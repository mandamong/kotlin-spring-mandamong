package com.mandamong.server.auth.service

import com.mandamong.server.auth.dto.RefreshRequest
import com.mandamong.server.auth.dto.RefreshResponse
import com.mandamong.server.auth.repository.RefreshTokenRepository
import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.common.error.exception.UnauthorizedException
import com.mandamong.server.common.util.jwt.TokenUtil
import com.mandamong.server.common.util.log.log
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RefreshService(
    private val tokenUtil: TokenUtil,
    private val refreshTokenRepository: RefreshTokenRepository,
) {

    @Transactional
    fun refresh(request: RefreshRequest): RefreshResponse {
        val userId = tokenUtil.parseRefreshToken(request.refreshToken).subject.toLong()
        val savedRefreshToken: String = refreshTokenRepository.get(userId) ?: throw IdNotFoundException(userId)
        validateToken(request.refreshToken, savedRefreshToken, userId)
        val newAccessToken: String = tokenUtil.generateAccessToken(userId)
        val newRefreshToken: String = tokenUtil.generateRefreshToken(userId)
        refreshTokenRepository.set(userId, newRefreshToken)
        log().info("TOKEN_REFRESHED userId=$userId")
        return RefreshResponse(userId, newAccessToken, newRefreshToken)
    }

    private fun validateToken(refreshToken: String, savedRefreshToken: String, userId: Long) {
        val savedUserId: Long = tokenUtil.parseRefreshToken(savedRefreshToken).subject.toLong()
        if (userId != savedUserId || refreshToken != savedRefreshToken) {
            throw UnauthorizedException()
        }
    }

}
