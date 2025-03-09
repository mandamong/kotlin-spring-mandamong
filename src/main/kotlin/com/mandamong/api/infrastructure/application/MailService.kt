package com.mandamong.api.infrastructure.application

import com.mandamong.api.domain.auth.api.dto.EmailVerificationResponse
import com.mandamong.api.domain.auth.dao.MemberRepository
import com.mandamong.api.domain.auth.domain.Member
import com.mandamong.api.domain.model.Email
import java.security.SecureRandom
import java.time.Duration
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MailService(
    private val memberRepository: MemberRepository,
    private val redisService: RedisService,
    private val javaMailSender: JavaMailSender,
) {
    @Transactional
    fun send(
        targetEmail: String,
        subject: String,
        text: String,
    ) {
        val emailForm: SimpleMailMessage = createEmailForm(targetEmail, subject, text)
        try {
            javaMailSender.send(emailForm)
        } catch (exception: RuntimeException) {
            throw IllegalStateException("이메일 전송 실패")
        }
    }

    @Transactional
    fun sendCode(targetEmail: String) {
        this.checkDuplicatedEmail(targetEmail)
        val subject = "만다몽 이메일 인증 번호"
        val code: String = createCode()
        send(targetEmail, subject, code)
        redisService.setValues(
            "email::auth::code::$targetEmail",
            code, Duration.ofMinutes(10)
        )
    }

    fun verifyCode(email: String, code: String): EmailVerificationResponse {
        checkDuplicatedEmail(email)
        val codeInRedis: String = redisService.getValues("email::auth::code::$email")
        val result: Boolean = redisService.checkExistsValue(codeInRedis) && codeInRedis == code

        return EmailVerificationResponse(result)
    }

    private fun createEmailForm(
        targetEmail: String,
        subject: String,
        text: String,
    ): SimpleMailMessage {
        val message: SimpleMailMessage = SimpleMailMessage()
        message.setTo(targetEmail)
        message.subject = subject
        message.text = text
        return message
    }

    private fun checkDuplicatedEmail(email: String) {
        val member: Member = memberRepository.findByEmail(Email.from(email))
            ?: return

        throw IllegalStateException("이메일: $email 중복 오류")
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
}
