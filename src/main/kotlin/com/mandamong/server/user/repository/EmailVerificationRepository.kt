package com.mandamong.server.user.repository

import java.time.Duration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository

@Repository
class EmailVerificationRepository(
    private val redisTemplate: StringRedisTemplate,
) {

    fun set(email: String, code: String) {
        redisTemplate.opsForValue()
            .set("$EMAIL_VERIFICATION_PREFIX$email", code, Duration.ofMinutes(EMAIL_VERIFICATION_TTL))
    }

    fun get(email: String): String? {
        return redisTemplate.opsForValue()
            .get("$EMAIL_VERIFICATION_PREFIX$email")
    }

    companion object {
        private const val EMAIL_VERIFICATION_PREFIX = "EMAIL::AUTH::CODE::"
        private const val EMAIL_VERIFICATION_TTL: Long = 5
    }

}
