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
import com.mandamong.server.user.dto.CreateUserRequest
import com.mandamong.server.user.dto.UpdateUserRequest
import com.mandamong.server.user.entity.User
import com.mandamong.server.user.model.Email
import com.mandamong.server.user.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val repository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val tokenUtil: TokenUtil,
    private val minioService: MinioService,
    private val refreshTokenRepository: RefreshTokenRepository,
) {

    private val log = log()

    @Transactional
    fun create(createUserRequest: CreateUserRequest): LoginResponse {
        validateEmailDuplication(createUserRequest.email)
        validateNicknameDuplication(createUserRequest.nickname)
        val encodedPassword = passwordEncoder.encode(createUserRequest.password)
        val user = createUserRequest.toEntity(encodedPassword)
        val savedUser = repository.save(user)

        createUserRequest.image?.let { savedUser.imageKey = minioService.upload(savedUser.id, it) }

        val presignedUrl = minioService.getPresignedUrlByObjectKey(savedUser.imageKey)
        val accessToken = tokenUtil.createAccessToken(savedUser.id)
        val refreshToken = tokenUtil.createRefreshToken(savedUser.id)
        refreshTokenRepository.set(savedUser.id, refreshToken)
        log.info("CREATE userId=${savedUser.id}")
        return LoginResponse.from(savedUser, presignedUrl, accessToken, refreshToken)
    }

    @Transactional
    fun update(request: UpdateUserRequest, userId: Long): String? {
        val user = getById(userId)
        var presignedUrl: String? = null
        request.nickname?.let { user.nickname = it }
        request.password?.let { user.password = passwordEncoder.encode(it) }
        request.image?.let {
            minioService.deleteObject(user.imageKey)
            user.imageKey = minioService.upload(userId, it)
            presignedUrl = minioService.getPresignedUrlByObjectKey(user.imageKey)
        }
        log.info("UPDATE userId=$userId")
        return presignedUrl
    }

    @Transactional
    fun delete(userId: Long) {
        val user = getById(userId)
        repository.deleteById(userId)
        refreshTokenRepository.delete(user.id)
        minioService.deleteObject(user.imageKey)
        log.info("DELETE userId=$userId")
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
        log.info("VALIDATE_PASSWORD userId=$userId")
        if (!isValidPassword(password, user.password)) {
            throw UnauthorizedException()
        }
    }

    @Transactional
    fun initializePassword(email: String): UpdateUserRequest {
        val user = getByEmail(email)
        val randomPassword = generateRandomPassword()
        user.password = passwordEncoder.encode(randomPassword)
        log.info("INITIALIZE_PASSWORD email=$email")
        return UpdateUserRequest(password = randomPassword)
    }

    private fun isValidPassword(raw: String, encoded: String) = passwordEncoder.matches(raw, encoded)

    private fun generateRandomPassword(length: Int = 12): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-=!@#$%^&*()_+"
        return (1..length).map { chars.random() }.joinToString("")
    }

}
