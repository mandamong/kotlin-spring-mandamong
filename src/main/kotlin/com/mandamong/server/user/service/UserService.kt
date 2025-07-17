package com.mandamong.server.user.service

import com.mandamong.server.auth.dto.EmailLoginResponse
import com.mandamong.server.common.error.exception.EmailDuplicatedException
import com.mandamong.server.common.error.exception.EmailNotFoundException
import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.common.error.exception.NicknameDuplicatedException
import com.mandamong.server.common.util.jwt.JwtUtil
import com.mandamong.server.infrastructure.minio.MinioService
import com.mandamong.server.infrastructure.redis.RedisService
import com.mandamong.server.user.dto.EmailRegisterRequest
import com.mandamong.server.user.dto.UserUpdateRequest
import com.mandamong.server.user.entity.Email
import com.mandamong.server.user.entity.User
import com.mandamong.server.user.repository.UserRepository
import java.time.Duration
import kotlin.jvm.optionals.getOrNull
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
        validateEmail(emailRegisterRequest.email)
        validateNickname(emailRegisterRequest.nickname)
        val encodedPassword = passwordEncoder.encode(emailRegisterRequest.password)
        val profileImageUrl = minioService.upload(emailRegisterRequest.profileImage, emailRegisterRequest.nickname)
        val user = EmailRegisterRequest.toEntity(emailRegisterRequest, encodedPassword, profileImageUrl)

        val savedUser = repository.save(user)
        val accessToken = jwtUtil.generateAccessToken(savedUser.id)
        val refreshToken = jwtUtil.generateRefreshToken(savedUser.id)
        redisService.set("RT::${savedUser.id}", refreshToken, Duration.ofDays(30))

        return User.toDto(savedUser, accessToken, refreshToken)
    }

    @Transactional
    fun updateNickname(request: UserUpdateRequest, id: Long): UserUpdateRequest {
        val user = getById(id)
        user.nickname = request.updated
        return UserUpdateRequest(updated = user.nickname)
    }

    @Transactional
    fun deleteById(id: Long) = repository.deleteById(id)

    @Transactional
    fun findById(id: Long): User? = repository.findById(id).getOrNull()

    @Transactional
    fun getById(id: Long): User = findById(id) ?: throw IdNotFoundException(id)

    @Transactional
    fun findByEmail(email: String): User? = repository.findByEmail(Email.from(email))

    @Transactional
    fun getByEmail(email: String): User = findByEmail(email) ?: throw EmailNotFoundException(email)

    @Transactional
    fun existsByEmail(email: String): Boolean = repository.existsByEmail(Email.from(email))

    @Transactional
    fun existsByNickname(nickname: String): Boolean = repository.existsByNickname(nickname)

    @Transactional
    fun validateEmail(email: String) {
        if (existsByEmail(email)) {
            throw EmailDuplicatedException(email)
        }
    }

    @Transactional
    fun validateNickname(nickname: String) {
        if (existsByNickname(nickname)) {
            throw NicknameDuplicatedException(nickname)
        }
    }

}
