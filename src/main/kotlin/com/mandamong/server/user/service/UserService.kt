package com.mandamong.server.user.service

import com.mandamong.server.auth.dto.response.EmailAuthResponse
import com.mandamong.server.common.util.jwt.JwtUtil
import com.mandamong.server.infrastructure.minio.MinioService
import com.mandamong.server.infrastructure.redis.RedisService
import com.mandamong.server.user.dto.request.EmailRegisterRequest
import com.mandamong.server.user.entity.Email
import com.mandamong.server.user.entity.User
import com.mandamong.server.user.repository.UserRepository
import java.time.Duration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val repository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val minioService: MinioService,
    private val redisService: RedisService,
) {

    @Transactional
    fun basicRegister(emailRegisterRequest: EmailRegisterRequest): EmailAuthResponse {
        if (isDuplicatedEmail(emailRegisterRequest.email)) {
            throw IllegalStateException("이메일: ${emailRegisterRequest.email} 중복 오류")
        }
        val encodedPassword: String = passwordEncoder.encode(emailRegisterRequest.password)
        val preSignedUrl: String = minioService.upload(emailRegisterRequest.profileImage, emailRegisterRequest.nickname)
        val user: User = User.toEntity(emailRegisterRequest, encodedPassword, preSignedUrl)
        val savedUser: User = repository.save(user)

        val accessToken: String = jwtUtil.generateAccessToken(savedUser.id)
        redisService.set(savedUser.id.toString(), accessToken, Duration.ofMinutes(10))
        val refreshToken: String = jwtUtil.generateRefreshToken(savedUser.id)
        savedUser.refreshToken = refreshToken
        return User.toDto(savedUser, accessToken, refreshToken)
    }

    fun findByRefreshToken(refreshToken: String): User? = repository.findByRefreshToken(refreshToken)
    fun findByEmail(email: String): User? = repository.findByEmail(Email.from(email))
    fun existsByNickname(nickname: String): Boolean = repository.existsByNickname(nickname)
    fun existsByEmail(email: String): Boolean = repository.existsByEmail(Email.from(email))
    fun isDuplicatedEmail(email: String) = repository.existsByEmail(Email.from(email))

}
