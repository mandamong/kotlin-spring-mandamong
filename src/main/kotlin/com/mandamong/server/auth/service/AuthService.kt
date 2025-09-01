package com.mandamong.server.auth.service

import com.mandamong.server.auth.dto.LoginResponse
import com.mandamong.server.auth.repository.RefreshTokenRepository
import com.mandamong.server.common.util.jwt.TokenUtil
import com.mandamong.server.common.util.log.log
import com.mandamong.server.infrastructure.minio.service.MinioService
import com.mandamong.server.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userService: UserService,
    private val tokenUtil: TokenUtil,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val minioService: MinioService,
) {
    @Transactional
    fun login(
        email: String,
        password: String,
    ): LoginResponse {
        val savedUser = userService.getByEmail(email)
        userService.validatePassword(password, savedUser.id)
        val accessToken = tokenUtil.createAccessToken(savedUser.id)
        val refreshToken = tokenUtil.createRefreshToken(savedUser.id)
        refreshTokenRepository.set(savedUser.id, refreshToken)
        val presignedUrl = minioService.getPresignedUrlByObjectKey(savedUser.imageKey)
        log().info("USER_LOGIN userId=${savedUser.id}")

        return LoginResponse.of(savedUser, presignedUrl, accessToken, refreshToken)
    }

    fun logout(userId: Long) {
        refreshTokenRepository.delete(userId)
        log().info("USER_LOGOUT userId=$userId")
    }
}
