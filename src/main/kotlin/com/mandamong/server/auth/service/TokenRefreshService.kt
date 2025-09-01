package com.mandamong.server.auth.service

import com.mandamong.server.auth.dto.TokenRefreshResponse
import com.mandamong.server.auth.repository.RefreshTokenRepository
import com.mandamong.server.common.error.exception.BadRequestException
import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.common.util.jwt.TokenUtil
import com.mandamong.server.common.util.log.log
import org.springframework.stereotype.Service

@Service
class TokenRefreshService(
    private val tokenUtil: TokenUtil,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    fun refresh(refreshToken: String): TokenRefreshResponse {
        val userId = tokenUtil.parseRefreshToken(refreshToken).subject.toLong()
        val savedRefreshToken: String = refreshTokenRepository.get(userId) ?: throw IdNotFoundException(userId)
        validateToken(refreshToken, savedRefreshToken, userId)
        val newAccessToken: String = tokenUtil.createAccessToken(userId)
        val newRefreshToken: String = tokenUtil.createRefreshToken(userId)
        refreshTokenRepository.set(userId, newRefreshToken)
        log().info("TOKEN_REFRESHED userId=$userId")
        return TokenRefreshResponse(userId, newAccessToken, newRefreshToken)
    }

    private fun validateToken(
        refreshToken: String,
        savedRefreshToken: String,
        userId: Long,
    ) {
        val savedUserId: Long = tokenUtil.parseRefreshToken(savedRefreshToken).subject.toLong()
        if (userId != savedUserId || refreshToken != savedRefreshToken) {
            throw BadRequestException()
        }
    }
}
