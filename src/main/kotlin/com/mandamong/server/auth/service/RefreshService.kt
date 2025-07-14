package com.mandamong.server.auth.service

import com.mandamong.server.auth.dto.request.RefreshRequest
import com.mandamong.server.auth.dto.response.RefreshResponse
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

        val savedRefreshToken: String = redisService.get("RT::$userId")
            ?: throw IllegalStateException("User ID 조회 오류")
        val savedUserId: Long = jwtUtil.parseRefreshToken(savedRefreshToken).subject.toLong()

        if (userId != savedUserId) {
            throw IllegalStateException("Refresh Token 검증 오류")
        }

        val newAccessToken: String = jwtUtil.generateAccessToken(userId)
        val newRefreshToken: String = jwtUtil.generateRefreshToken(userId)
        redisService.set("RT::$userId", newRefreshToken, Duration.ofDays(30))

        return RefreshResponse(userId, newAccessToken, newRefreshToken)
    }

}
