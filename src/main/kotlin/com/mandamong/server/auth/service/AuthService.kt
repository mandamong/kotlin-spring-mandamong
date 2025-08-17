package com.mandamong.server.auth.service

import com.mandamong.server.auth.dto.LoginRequest
import com.mandamong.server.auth.dto.LoginResponse
import com.mandamong.server.auth.repository.RefreshTokenRepository
import com.mandamong.server.common.error.exception.UnauthorizedException
import com.mandamong.server.common.util.cookie.CookieUtil
import com.mandamong.server.common.util.jwt.TokenUtil
import com.mandamong.server.common.util.log.log
import com.mandamong.server.infrastructure.minio.MinioService
import com.mandamong.server.user.dto.LoginUser
import com.mandamong.server.user.entity.User
import com.mandamong.server.user.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userService: UserService,
    private val tokenUtil: TokenUtil,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val minioService: MinioService,
    private val cookieUtil: CookieUtil,
) {

    @Transactional
    fun login(request: LoginRequest, response: HttpServletResponse): LoginResponse {
        val savedUser = userService.getByEmail(request.email)
        validatePassword(request.password, savedUser.password)
        val accessToken = tokenUtil.createAccessToken(savedUser.id)
        val refreshToken = tokenUtil.createRefreshToken(savedUser.id)
        refreshTokenRepository.set(savedUser.id, refreshToken)
        val presignedUrl = minioService.getPresignedUrlByObjectKey(savedUser.imageKey)
        cookieUtil.add(response, "access_token", accessToken, 300)
        cookieUtil.add(response, "refresh_token", refreshToken, 2_592_000)
        log().info("USER_LOGIN userId=${savedUser.id}")
        return savedUser.toDto(presignedUrl, accessToken, refreshToken)
    }

    @Transactional
    fun logout(loginUser: LoginUser, request: HttpServletRequest, response: HttpServletResponse) {
        refreshTokenRepository.delete(loginUser.userId)
        cookieUtil.delete(request, response, "access_token")
        log().info("USER_LOGOUT userId=${loginUser.userId}")
    }

    private fun validatePassword(raw: String, encoded: String) {
        if (!passwordEncoder.matches(raw, encoded)) {
            throw UnauthorizedException()
        }
    }

}
