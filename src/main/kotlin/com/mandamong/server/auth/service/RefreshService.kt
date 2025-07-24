package com.mandamong.server.auth.service

import com.mandamong.server.auth.dto.RefreshRequest
import com.mandamong.server.auth.dto.RefreshResponse
import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.common.error.exception.UnauthorizedException
import com.mandamong.server.common.util.jwt.TokenUtil
import com.mandamong.server.common.util.log.log
import java.time.Duration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RefreshService(
    private val tokenUtil: TokenUtil,
    private val redisTemplate: StringRedisTemplate,
) {

    @Transactional
    fun refresh(request: RefreshRequest): RefreshResponse {
        val userId = tokenUtil.parseRefreshToken(request.refreshToken).subject.toLong()
        val savedRefreshToken: String = redisTemplate.opsForValue().get("RT::$userId")
            ?: throw IdNotFoundException(userId)
        val savedUserId: Long = tokenUtil.parseRefreshToken(savedRefreshToken).subject.toLong()
        validateId(userId, savedUserId)
        val newAccessToken: String = tokenUtil.generateAccessToken(userId)
        val newRefreshToken: String = tokenUtil.generateRefreshToken(userId)
        redisTemplate.opsForValue().set("RT::$userId", newRefreshToken, Duration.ofDays(30))
        log().info("TOKEN_REFRESHED userId=$userId")
        return RefreshResponse(userId, newAccessToken, newRefreshToken)
    }

    private fun validateId(userId: Long, savedUserId: Long) {
        if (userId != savedUserId) {
            throw UnauthorizedException()
        }
    }

}
