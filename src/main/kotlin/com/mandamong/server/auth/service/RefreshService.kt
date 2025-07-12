package com.mandamong.server.auth.service

import com.mandamong.server.auth.dto.response.RefreshResponse
import com.mandamong.server.common.util.jwt.JwtUtil
import com.mandamong.server.infrastructure.redis.RedisService
import com.mandamong.server.user.entity.User
import com.mandamong.server.user.service.UserService
import java.time.Duration
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RefreshService(
    private val userService: UserService,
    private val jwtUtil: JwtUtil,
    private val redisService: RedisService,
) {

    @Transactional
    fun refresh(refreshToken: String): RefreshResponse {
        val user: User = userService.findByRefreshToken(refreshToken)
            ?: throw IllegalArgumentException("Refresh Token 조회 오류")

        validateMemberIdInRefreshToken(refreshToken, user.id)
        val newAccessToken: String = jwtUtil.generateAccessToken(user.id)
        redisService.set(user.id.toString(), newAccessToken, Duration.ofMinutes(10))
        val newRefreshToken: String = jwtUtil.generateRefreshToken(user.id)
        user.refreshToken = newRefreshToken
        return RefreshResponse(user.id, newAccessToken, newRefreshToken)
    }

    private fun validateMemberIdInRefreshToken(refreshToken: String, userId: Long) {
        val savedUserId = userService.findByRefreshToken(refreshToken)!!.id
        if (userId != savedUserId) {
            throw IllegalStateException("Refresh Token, Member Id 검증 오류")
        }
    }

}
