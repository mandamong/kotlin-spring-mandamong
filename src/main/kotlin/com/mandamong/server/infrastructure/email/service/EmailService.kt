package com.mandamong.server.infrastructure.email.service

import com.mandamong.server.common.error.exception.BadRequestException
import com.mandamong.server.common.error.exception.TimeoutException
import com.mandamong.server.common.util.log.log
import com.mandamong.server.infrastructure.email.entity.EmailOutbox
import com.mandamong.server.infrastructure.email.repository.EmailOutboxRepository
import com.mandamong.server.user.dto.ValidateEmailRequest
import com.mandamong.server.user.repository.EmailVerificationRepository
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmailService(
    private val emailBuilder: EmailBuilder,
    private val repository: EmailOutboxRepository,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val mailSender: JavaMailSender,
) {

    @Transactional
    fun sendCode(request: ValidateEmailRequest) {
        val outbox = EmailOutbox(email = request.email, code = emailBuilder.createCode())
        repository.save(outbox)
    }

    fun verifyCode(email: String, code: String) {
        val savedCode: String? = emailVerificationRepository.get(email)
        if (savedCode == null) {
            throw TimeoutException()
        } else if (savedCode != code) {
            throw BadRequestException()
        }

        log().info("EMAIL_VERIFIED email=$email")
    }

    fun sendEmail(email: String, code: String) {
        val message: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")
        helper.setTo(email)
        helper.setSubject(EMAIL_SUBJECT)
        helper.setText(emailBuilder.createEmail(code), true)
        mailSender.send(message)
        log().info("VERIFICATION_EMAIL_SENT email=$email")
    }

    companion object {
        private const val EMAIL_SUBJECT: String = "만다몽 - 이메일 인증 번호"
    }

}
