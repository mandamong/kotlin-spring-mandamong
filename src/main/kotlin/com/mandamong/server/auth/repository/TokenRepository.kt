package com.mandamong.server.auth.repository

import com.mandamong.server.common.util.jwt.TokenUtil
import java.time.Duration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository

@Repository
class TokenRepository(
    private val redisTemplate: StringRedisTemplate,
    private val tokenUtil: TokenUtil,
) {

    fun set(userId: Long, refreshToken: String) {
        redisTemplate.opsForValue()
            .set("$REFRESH_TOKEN_PREFIX$userId", refreshToken, Duration.ofMillis(tokenUtil.properties.refreshExpiry))
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
