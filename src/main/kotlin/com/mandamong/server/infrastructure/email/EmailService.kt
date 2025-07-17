package com.mandamong.server.infrastructure.email

import com.mandamong.server.auth.dto.EmailVerificationResponse
import com.mandamong.server.common.error.exception.BusinessBaseException
import com.mandamong.server.infrastructure.redis.RedisService
import java.security.SecureRandom
import java.time.Duration
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmailService(
    private val redisService: RedisService,
    private val mailSender: JavaMailSender,
) {

    @Transactional
    fun sendCode(email: String) {
        val code = createCode()
        sendEmail(email, code)
        redisService.set(REDIS_PREFIX + email, code, Duration.ofMinutes(5))
    }

    @Transactional
    fun verifyCode(email: String, code: String): EmailVerificationResponse {
        val savedCode: String? = redisService.get(REDIS_PREFIX + email)
        val result: Boolean = savedCode != null && savedCode == code
        return EmailVerificationResponse(result)
    }

    private fun createCode(): String {
        val length = CODE_LENGTH
        val random: SecureRandom = SecureRandom.getInstanceStrong()
        val code = StringBuilder()
        for (i in 0..<length) {
            code.append(random.nextInt(10))
        }
        return code.toString()
    }

    private fun sendEmail(email: String, text: String) {
        val mailMessage: SimpleMailMessage = createEmail(email, text)
        try {
            mailSender.send(mailMessage)
        } catch (exception: RuntimeException) {
            throw BusinessBaseException()
        }
    }

    private fun createEmail(targetEmail: String, text: String): SimpleMailMessage {
        val message = SimpleMailMessage()
        message.setTo(targetEmail)
        message.subject = EMAIL_SUBJECT
        message.text = text
        return message
    }

    companion object {
        private const val REDIS_PREFIX: String = "email::auth::code::"
        private const val EMAIL_SUBJECT: String = "만다몽 이메일 인증 번호"
        private const val CODE_LENGTH = 6
    }

}
