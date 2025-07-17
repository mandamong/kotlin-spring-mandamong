package com.mandamong.server.auth.service

import com.mandamong.server.auth.dto.RefreshRequest
import com.mandamong.server.auth.dto.RefreshResponse
import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.common.error.exception.UnauthorizedException
import com.mandamong.server.common.util.jwt.JwtUtil
import com.mandamong.server.infrastructure.redis.RedisService
import java.time.Duration
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RefreshService(
    private val jwtUtil: JwtUtil,
    private val redisService: RedisService,
) {

    @Transactional
    fun refresh(request: RefreshRequest): RefreshResponse {
        val userId = jwtUtil.parseRefreshToken(request.refreshToken).subject.toLong()
        val savedRefreshToken: String = redisService.get("RT::$userId") ?: throw IdNotFoundException(userId)
        val savedUserId: Long = jwtUtil.parseRefreshToken(savedRefreshToken).subject.toLong()

        validateId(userId, savedUserId)

        val newAccessToken: String = jwtUtil.generateAccessToken(userId)
        val newRefreshToken: String = jwtUtil.generateRefreshToken(userId)
        redisService.set("RT::$userId", newRefreshToken, Duration.ofDays(30))

        return RefreshResponse(userId, newAccessToken, newRefreshToken)
    }

    private fun validateId(userId: Long, savedUserId: Long) {
        if (userId != savedUserId) {
            throw UnauthorizedException()
        }
    }

}
