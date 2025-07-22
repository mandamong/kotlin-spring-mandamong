package com.mandamong.server.auth.service

import com.mandamong.server.auth.dto.EmailLoginResponse
import com.mandamong.server.common.error.exception.UnauthorizedException
import com.mandamong.server.common.util.jwt.TokenUtil
import com.mandamong.server.common.util.log.log
import com.mandamong.server.infrastructure.redis.RedisService
import com.mandamong.server.user.entity.User
import com.mandamong.server.user.service.UserService
import java.time.Duration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userService: UserService,
    private val tokenUtil: TokenUtil,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val redisService: RedisService,
) {

    @Transactional
    fun basicLogin(email: String, password: String): EmailLoginResponse {
        val savedUser = userService.getByEmail(email)
        validatePassword(password, savedUser.password)
        val accessToken = tokenUtil.generateAccessToken(savedUser.id)
        val refreshToken = tokenUtil.generateRefreshToken(savedUser.id)
        redisService.set("RT::${savedUser.id}", refreshToken, Duration.ofMillis(tokenUtil.getRefreshExpiry()))
        log().info("USER_LOGIN userId=${savedUser.id}")
        return User.toDto(savedUser, accessToken, refreshToken)
    }

    @Transactional
    fun logout(userId: Long) {
        redisService.delete("RT::$userId")
        log().info("USER_LOGOUT userId=$userId")
    }

    private fun validatePassword(password: String, savedPassword: String) {
        if (!passwordEncoder.matches(password, savedPassword)) {
            throw UnauthorizedException()
        }
    }

}
