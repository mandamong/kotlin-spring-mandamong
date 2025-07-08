package com.mandamong.api.infrastructure.application

import com.mandamong.api.domain.auth.api.dto.EmailVerificationResponse
import java.security.SecureRandom
import java.time.Duration
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmailService(
    private val redisService: RedisService,
    private val javaMailSender: JavaMailSender,
) {

    @Transactional
    fun sendCode(targetEmail: String) {
        val code: String = createCode()
        send(targetEmail, code)
        redisService.setValues(
            REDIS_PREFIX + targetEmail,
            code,
            Duration.ofMinutes(10),
        )
    }

    @Transactional
    fun verifyCode(email: String, code: String): EmailVerificationResponse {
        val codeInRedis: String = redisService.getValues(REDIS_PREFIX + email)

        val result: Boolean = redisService.checkExistsValue(codeInRedis) && codeInRedis == code

        return EmailVerificationResponse(result)
    }

    private fun createCode(): String {
        val length: Int = 6
        val random: SecureRandom = SecureRandom.getInstanceStrong()
        val stringBuilder = StringBuilder()
        for (i in 0..<length) {
            stringBuilder.append(random.nextInt(10))
        }
        return stringBuilder.toString()
    }

    private fun send(
        targetEmail: String,
        text: String,
    ) {
        val email: SimpleMailMessage = createEmail(targetEmail, text)
        try {
            javaMailSender.send(email)
        } catch (exception: RuntimeException) {
            throw IllegalStateException("이메일 전송 실패")
        }
    }

    private fun createEmail(
        targetEmail: String,
        text: String,
    ): SimpleMailMessage {
        val message: SimpleMailMessage = SimpleMailMessage()
        message.setTo(targetEmail)
        message.subject = EMAIL_SUBJECT
        message.text = text
        return message
    }

    companion object {
        private const val REDIS_PREFIX: String = "email::auth::code::"
        private const val EMAIL_SUBJECT: String = "만다몽 이메일 인증 번호"
    }
}
