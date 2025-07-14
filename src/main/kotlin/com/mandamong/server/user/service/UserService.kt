package com.mandamong.server.user.service

import com.mandamong.server.auth.dto.response.EmailLoginResponse
import com.mandamong.server.common.util.jwt.JwtUtil
import com.mandamong.server.infrastructure.minio.MinioService
import com.mandamong.server.infrastructure.redis.RedisService
import com.mandamong.server.user.dto.request.EmailRegisterRequest
import com.mandamong.server.user.dto.request.UserUpdateRequest
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
    fun basicRegister(emailRegisterRequest: EmailRegisterRequest): EmailLoginResponse {
        if (isDuplicatedEmail(emailRegisterRequest.email)) {
            throw IllegalStateException("이메일: ${emailRegisterRequest.email} 중복 오류")
        }
        val encodedPassword = passwordEncoder.encode(emailRegisterRequest.password)
        val profileImageUrl = minioService.upload(emailRegisterRequest.profileImage, emailRegisterRequest.nickname)
        val user = User.toEntity(emailRegisterRequest, encodedPassword, profileImageUrl)
        val savedUser = repository.save(user)

        val accessToken = jwtUtil.generateAccessToken(savedUser.id)
        val refreshToken = jwtUtil.generateRefreshToken(savedUser.id)
        redisService.set("RT::${savedUser.id}", refreshToken, Duration.ofDays(30))

        return User.toDto(savedUser, accessToken, refreshToken)
    }

    @Transactional
    fun updateNickname(request: UserUpdateRequest, userId: Long): UserUpdateRequest {
        val user = repository.findById(userId).orElseThrow()
        user.nickname = request.updated
        return UserUpdateRequest(updated = user.nickname)
    }

    fun findById(id: Long): User = repository.findById(id).orElseThrow()
    fun findByEmail(email: String): User? = repository.findByEmail(Email.from(email))
    fun existsByNickname(nickname: String): Boolean = repository.existsByNickname(nickname)
    fun existsByEmail(email: String): Boolean = repository.existsByEmail(Email.from(email))
    fun isDuplicatedEmail(email: String) = repository.existsByEmail(Email.from(email))

}
