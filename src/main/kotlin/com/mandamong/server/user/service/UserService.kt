package com.mandamong.server.user.service

import com.mandamong.server.auth.dto.LoginResponse
import com.mandamong.server.auth.repository.RefreshTokenRepository
import com.mandamong.server.common.error.exception.EmailDuplicatedException
import com.mandamong.server.common.error.exception.EmailNotFoundException
import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.common.error.exception.NicknameDuplicatedException
import com.mandamong.server.common.error.exception.UnauthorizedException
import com.mandamong.server.common.util.jwt.TokenUtil
import com.mandamong.server.common.util.log.log
import com.mandamong.server.infrastructure.minio.service.MinioService
import com.mandamong.server.user.dto.RegisterRequest
import com.mandamong.server.user.dto.UserUpdateRequest
import com.mandamong.server.user.entity.User
import com.mandamong.server.user.model.Email
import com.mandamong.server.user.repository.UserRepository
import kotlin.jvm.optionals.getOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val repository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val tokenUtil: TokenUtil,
    private val minioService: MinioService,
    private val refreshTokenRepository: RefreshTokenRepository,
) {

    @Transactional
    fun create(registerRequest: RegisterRequest): LoginResponse {
        validateEmailDuplication(registerRequest.email)
        validateNicknameDuplication(registerRequest.nickname)
        val encodedPassword = passwordEncoder.encode(registerRequest.password)
        val user = registerRequest.toEntity(encodedPassword)
        val savedUser = repository.save(user)

        registerRequest.image?.let { savedUser.imageKey = minioService.upload(savedUser.id, it) }

        val presignedUrl: String = minioService.getPresignedUrlByObjectKey(savedUser.imageKey)
        val accessToken = tokenUtil.createAccessToken(savedUser.id)
        val refreshToken = tokenUtil.createRefreshToken(savedUser.id)
        refreshTokenRepository.set(savedUser.id, refreshToken)
        log().info("REGISTER userId=${savedUser.id}")
        return savedUser.toDto(presignedUrl, accessToken, refreshToken)
    }

    @Transactional
    fun updateNickname(nickname: String, userId: Long): UserUpdateRequest {
        val user = getById(userId)
        user.nickname = nickname
        log().info("UPDATE_NICKNAME userId=$userId")
        return UserUpdateRequest(updated = user.nickname)
    }

    @Transactional
    fun delete(userId: Long) {
        repository.deleteById(userId)
        log().info("UNREGISTER userId=$userId")
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
    fun validatePassword(password: String, userId: Long) {
        val user = getById(userId)
        log().info("VALIDATE_PASSWORD userId=$userId")
        if (!isValidPassword(password, user)) {
            throw UnauthorizedException()
        }
    }

    @Transactional
    fun updatePassword(password: String, userId: Long) {
        val user = getById(userId)
        user.password = passwordEncoder.encode(password)
        log().info("UPDATE_PASSWORD userId=$userId")
    }

    @Transactional
    fun initializePassword(email: String): UserUpdateRequest {
        val user = getByEmail(email)
        val randomPassword = generateRandomPassword()
        user.password = passwordEncoder.encode(randomPassword)
        log().info("INITIALIZE_PASSWORD email=$email")
        return UserUpdateRequest(updated = randomPassword)
    }

    private fun isValidPassword(password: String, user: User) = passwordEncoder.matches(password, user.password)

    private fun generateRandomPassword(length: Int = 12): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-=!@#$%^&*()_+"
        return (1..length).map { chars.random() }.joinToString("")
    }

}
