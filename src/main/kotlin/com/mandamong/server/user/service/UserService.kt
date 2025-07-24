package com.mandamong.server.user.service

import com.mandamong.server.auth.dto.LoginResponse
import com.mandamong.server.common.error.exception.EmailDuplicatedException
import com.mandamong.server.common.error.exception.EmailNotFoundException
import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.common.error.exception.NicknameDuplicatedException
import com.mandamong.server.common.error.exception.UnauthorizedException
import com.mandamong.server.common.util.jwt.TokenUtil
import com.mandamong.server.common.util.log.log
import com.mandamong.server.infrastructure.minio.MinioService
import com.mandamong.server.user.dto.LoginUser
import com.mandamong.server.user.dto.PasswordValidationRequest
import com.mandamong.server.user.dto.RegisterRequest
import com.mandamong.server.user.dto.UserUpdateRequest
import com.mandamong.server.user.entity.Email
import com.mandamong.server.user.entity.User
import com.mandamong.server.user.repository.UserRepository
import java.time.Duration
import kotlin.jvm.optionals.getOrNull
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val repository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val tokenUtil: TokenUtil,
    private val minioService: MinioService,
    private val redisTemplate: StringRedisTemplate,
) {

    @Transactional
    fun register(registerRequest: RegisterRequest): LoginResponse {
        validateEmailDuplication(registerRequest.email)
        validateNicknameDuplication(registerRequest.nickname)
        val encodedPassword = passwordEncoder.encode(registerRequest.password)
        val profileImageUrl = minioService.upload(registerRequest.image, registerRequest.nickname)
        val user = RegisterRequest.toEntity(registerRequest, encodedPassword, profileImageUrl)

        val savedUser = repository.save(user)
        val accessToken = tokenUtil.generateAccessToken(savedUser.id)
        val refreshToken = tokenUtil.generateRefreshToken(savedUser.id)
        redisTemplate.opsForValue()
            .set("RT::${savedUser.id}", refreshToken, Duration.ofMillis(tokenUtil.properties.refreshExpiry))

        log().info("REGISTER userId=${savedUser.id}")
        return User.toDto(savedUser, accessToken, refreshToken)
    }

    @Transactional
    fun updateNickname(request: UserUpdateRequest, loginUser: LoginUser): UserUpdateRequest {
        val user = getById(loginUser.userId)
        user.nickname = request.updated
        log().info("UPDATE_NICKNAME userId=${loginUser.userId}")
        return UserUpdateRequest(updated = user.nickname)
    }

    @Transactional
    fun unregister(loginUser: LoginUser) {
        repository.deleteById(loginUser.userId)
        log().info("UNREGISTER userId=${loginUser.userId}")
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): User? = repository.findById(id).getOrNull()

    @Transactional(readOnly = true)
    fun getById(id: Long): User = findById(id) ?: throw IdNotFoundException(id)

    @Transactional(readOnly = true)
    fun findByEmail(email: String): User? = repository.findByEmail(Email.from(email))

    @Transactional(readOnly = true)
    fun getByEmail(email: String): User = findByEmail(email) ?: throw EmailNotFoundException(email)

    @Transactional(readOnly = true)
    fun validateEmailDuplication(email: String) {
        if (repository.existsByEmail(Email.from(email))) {
            throw EmailDuplicatedException(email)
        }
    }

    @Transactional(readOnly = true)
    fun validateNicknameDuplication(nickname: String) {
        if (repository.existsByNickname(nickname)) {
            throw NicknameDuplicatedException(nickname)
        }
    }

    @Transactional(readOnly = true)
    fun validatePassword(request: PasswordValidationRequest, loginUser: LoginUser) {
        val user = getById(loginUser.userId)
        log().info("VALIDATE_PASSWORD userId=${loginUser.userId}")
        if (!isValidPassword(request, user)) {
            throw UnauthorizedException()
        }
    }

    @Transactional
    fun updatePassword(request: UserUpdateRequest, loginUser: LoginUser) {
        val user = getById(loginUser.userId)
        user.password = passwordEncoder.encode(request.updated)
        log().info("UPDATE_PASSWORD userId=${loginUser.userId}")
    }

    @Transactional
    fun initializePassword(loginUser: LoginUser): UserUpdateRequest {
        val user = getById(loginUser.userId)
        val randomPassword = generateRandomPassword()
        user.password = passwordEncoder.encode(randomPassword)
        log().info("INITIALIZE_PASSWORD userId=${loginUser.userId}")
        return UserUpdateRequest(updated = randomPassword)
    }

    private fun isValidPassword(
        request: PasswordValidationRequest,
        user: User,
    ) = passwordEncoder.matches(request.password, user.password)

    private fun generateRandomPassword(length: Int = 12): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-=!@#$%^&*()_+"
        return (1..length).map { chars.random() }.joinToString("")
    }
}
