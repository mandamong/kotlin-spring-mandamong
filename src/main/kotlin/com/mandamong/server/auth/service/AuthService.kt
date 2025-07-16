package com.mandamong.server.auth.service

import com.mandamong.server.auth.dto.EmailLoginResponse
import com.mandamong.server.common.error.exception.EmailNotFoundException
import com.mandamong.server.common.util.jwt.JwtUtil
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
    private val jwtUtil: JwtUtil,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val redisService: RedisService,
) {

    @Transactional
    fun basicLogin(email: String, password: String): EmailLoginResponse {
        val user: User = userService.findByEmail(email) ?: throw EmailNotFoundException(email)

        if (isValidPassword(password, user.password)) {
            val accessToken: String = jwtUtil.generateAccessToken(user.id)
            val refreshToken: String = jwtUtil.generateRefreshToken(user.id)
            redisService.set("RT::${user.id}", refreshToken, Duration.ofDays(30))
            return User.toDto(user, accessToken, refreshToken)
        }

        throw IllegalArgumentException("비밀번호 검증 오류")
    }

    @Transactional
    fun logout(userId: Long) {
        redisService.delete("RT::$userId")
    }

    private fun isValidPassword(input: String, saved: String): Boolean = passwordEncoder.matches(input, saved)

}
