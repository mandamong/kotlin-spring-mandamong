package com.mandamong.server.infrastructure.email.service

import com.mandamong.server.common.error.exception.UnauthorizedException
import com.mandamong.server.common.util.log.log
import com.mandamong.server.infrastructure.email.entity.EmailOutbox
import com.mandamong.server.infrastructure.email.repository.EmailOutboxRepository
import com.mandamong.server.user.dto.EmailVerificationRequest
import com.mandamong.server.user.repository.EmailVerificationRepository
import java.security.SecureRandom
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmailService(
    private val emailVerificationRepository: EmailVerificationRepository,
    private val repository: EmailOutboxRepository,
) {

    @Transactional
    fun sendCode(request: EmailVerificationRequest) {
        val outbox = EmailOutbox(email = request.email, code = createCode())
        repository.save(outbox)
    }

    fun verifyCode(email: String, code: String) {
        val savedCode: String? = emailVerificationRepository.get(email)
        if (savedCode == null || savedCode != code) {
            throw UnauthorizedException()
        }
        log().info("EMAIL_VERIFIED email=$email")
    }

    private fun createCode(): String {
        val random: SecureRandom = SecureRandom.getInstanceStrong()
        return buildString(CODE_LENGTH) { repeat(CODE_LENGTH) { append(random.nextInt(RANDOM_RANGE)) } }
    }

    companion object {
        private const val CODE_LENGTH = 6
        private const val RANDOM_RANGE = 10
    }

}
