package com.mandamong.server.infrastructure.email.scheduler

import com.mandamong.server.common.annotation.lock.DistributedLock
import com.mandamong.server.common.error.exception.BusinessBaseException
import com.mandamong.server.common.util.log.log
import com.mandamong.server.infrastructure.email.entity.EmailOutbox
import com.mandamong.server.infrastructure.email.enums.EmailOutboxStatus
import com.mandamong.server.infrastructure.email.repository.EmailOutboxRepository
import com.mandamong.server.user.repository.EmailVerificationRepository
import jakarta.mail.internet.MimeMessage
import java.util.concurrent.TimeUnit
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class EmailOutboxScheduler(
    private val repository: EmailOutboxRepository,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val mailSender: JavaMailSender,
) {

    @DistributedLock(name = "EMAIL", key = "'OUTBOX'", autoUnlockAfter = 30)
    @Transactional
    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    fun publish() {
        val outboxes = repository.findTop10ByStatus(EmailOutboxStatus.PENDING)
        outboxes.forEach { outbox ->
            try {
                sendEmail(outbox)
                outbox.markSent()
                emailVerificationRepository.set(outbox.email, outbox.code)
            } catch (e: Exception) {
                outbox.markFailed()
                throw BusinessBaseException()
            }
        }
    }

    private fun sendEmail(outbox: EmailOutbox) {
        val message: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")
        helper.setTo(outbox.email)
        helper.setSubject(EMAIL_SUBJECT)
        helper.setText(createEmail(outbox.code), true)
        mailSender.send(message)
        log().info("VERIFICATION_EMAIL_SENT email=${outbox.email}")
    }

    private fun createEmail(code: String): String {
        return """
            <div style="font-family: 'Apple SD Gothic Neo', Arial, sans-serif; background-color: #f5f7fa; padding: 60px 20px;">
                <div style="max-width: 480px; margin: 0 auto; margin-top: 40px; background: white; border-radius: 10px; box-shadow: 0 6px 16px rgba(0,0,0,0.08); padding: 40px 30px; text-align: center;">
                    <h2 style="color: #2c3e50; margin-bottom: 24px;">이메일 인증 번호</h2>
                    <p style="font-size: 16px; color: #555;">아래 인증번호를 입력해주세요.</p>
                    <div style="font-size: 36px; font-weight: bold; letter-spacing: 8px; color: #ffb100; margin: 28px 0;">
                        $code
                    </div>
                    <p style="font-size: 14px; color: #888;">이 인증번호는 5분간 유효합니다.</p>
                </div>
                <div style="text-align: center; font-size: 12px; color: #aaa; margin-top: 40px;">
                    만다몽 팀 드림
                </div>
            </div>
        """.trimIndent()
    }

    companion object {
        private const val EMAIL_SUBJECT: String = "만다몽 - 이메일 인증 번호"
    }

}
