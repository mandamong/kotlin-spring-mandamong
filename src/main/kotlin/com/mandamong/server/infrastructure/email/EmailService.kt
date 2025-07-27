package com.mandamong.server.infrastructure.email

import com.mandamong.server.user.repository.EmailVerificationRepository
import com.mandamong.server.common.error.exception.BusinessBaseException
import com.mandamong.server.common.error.exception.UnauthorizedException
import com.mandamong.server.common.util.log.log
import com.mandamong.server.user.dto.EmailVerificationRequest
import java.security.SecureRandom
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmailService(
    private val emailVerificationRepository: EmailVerificationRepository,
    private val mailSender: JavaMailSender,
) {

    @Transactional
    fun sendCode(request: EmailVerificationRequest) {
        val code = createCode()
        sendEmail(request.email, code)
        emailVerificationRepository.set(request.email, code)
        log().info("EMAIL_VERIFICATION_SENT email=${request.email}")
    }

    @Transactional
    fun verifyCode(email: String, code: String) {
        val savedCode: String? = emailVerificationRepository.get(email)
        require(savedCode == null || savedCode != code) { throw UnauthorizedException() }
        log().info("EMAIL_VERIFIED email=$email")
    }

    private fun createCode(): String {
        val random: SecureRandom = SecureRandom.getInstanceStrong()
        val code = StringBuilder()
        for (i in 0..<CODE_LENGTH) {
            code.append(random.nextInt(RANDOM_RANGE))
        }
        return code.toString()
    }

    private fun sendEmail(email: String, text: String) {
        val mailMessage: SimpleMailMessage = createEmail(email, text)
        try {
            mailSender.send(mailMessage)
        } catch (e: RuntimeException) {
            throw BusinessBaseException()
        }
    }

    private fun createEmail(email: String, text: String): SimpleMailMessage {
        val message = SimpleMailMessage()
        message.setTo(email)
        message.subject = EMAIL_SUBJECT
        message.text = text
        return message
    }

    companion object {
        private const val EMAIL_SUBJECT: String = "만다몽 이메일 인증 번호"
        private const val CODE_LENGTH = 6
        private const val RANDOM_RANGE = 10
    }

}
