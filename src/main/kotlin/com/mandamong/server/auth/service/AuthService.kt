package com.mandamong.server.auth.service

import com.mandamong.server.auth.dto.LoginRequest
import com.mandamong.server.auth.dto.LoginResponse
import com.mandamong.server.common.error.exception.UnauthorizedException
import com.mandamong.server.common.util.jwt.TokenUtil
import com.mandamong.server.common.util.log.log
import com.mandamong.server.infrastructure.minio.MinioService
import com.mandamong.server.user.dto.LoginUser
import com.mandamong.server.user.entity.User
import com.mandamong.server.user.service.UserService
import java.time.Duration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userService: UserService,
    private val tokenUtil: TokenUtil,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val redisTemplate: StringRedisTemplate,
    private val minioService: MinioService,
) {

    @Transactional
    fun login(request: LoginRequest): LoginResponse {
        val savedUser = userService.getByEmail(request.email)
        validatePassword(request.password, savedUser.password)
        val accessToken = tokenUtil.generateAccessToken(savedUser.id)
        val refreshToken = tokenUtil.generateRefreshToken(savedUser.id)
        redisTemplate.opsForValue()
            .set("RT::${savedUser.id}", refreshToken, Duration.ofMillis(tokenUtil.properties.refreshExpiry))
        savedUser.image = minioService.getPresignedUrlByNickname(savedUser.nickname)
        log().info("USER_LOGIN userId=${savedUser.id}")
        return User.toDto(savedUser, accessToken, refreshToken)
    }

    @Transactional
    fun logout(loginUser: LoginUser) {
        redisTemplate.delete("RT::${loginUser.userId}")
        log().info("USER_LOGOUT userId=${loginUser.userId}")
    }

    private fun validatePassword(raw: String, encoded: String) {
        if (!passwordEncoder.matches(raw, encoded)) {
            throw UnauthorizedException()
        }
    }

}
