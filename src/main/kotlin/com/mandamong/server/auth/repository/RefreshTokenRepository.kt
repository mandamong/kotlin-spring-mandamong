package com.mandamong.server.auth.repository

import com.mandamong.server.common.util.jwt.TokenProperties
import java.time.Duration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository

@Repository
class RefreshTokenRepository(
    private val redisTemplate: StringRedisTemplate,
    private val properties: TokenProperties,
) {

    fun set(userId: Long, refreshToken: String) {
        redisTemplate.opsForValue()
            .set("$REFRESH_TOKEN_PREFIX$userId", refreshToken, Duration.ofMillis(properties.expiry.refresh))
    }

    fun get(userId: Long): String? {
        return redisTemplate.opsForValue()
            .get("$REFRESH_TOKEN_PREFIX$userId")
    }

    fun delete(userId: Long) {
        redisTemplate.delete("$REFRESH_TOKEN_PREFIX$userId")
    }

    companion object {
        private const val REFRESH_TOKEN_PREFIX = "RT::"
    }

}
