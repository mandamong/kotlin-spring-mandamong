package com.mandamong.api.auth.application

import com.mandamong.api.auth.api.dto.EmailAuthResponse
import com.mandamong.api.auth.api.dto.EmailLoginRequest
import com.mandamong.api.auth.api.dto.EmailSignupRequest
import com.mandamong.api.auth.api.dto.RefreshRequest
import com.mandamong.api.auth.api.dto.RefreshResponse
import com.mandamong.api.auth.dao.MemberRepository
import com.mandamong.api.auth.domain.Member
import com.mandamong.api.auth.util.JwtUtil
import com.mandamong.api.infrastructure.application.MinioService
import com.mandamong.api.infrastructure.application.RedisService
import com.mandamong.api.model.Email
import java.time.Duration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val jwtUtil: JwtUtil,
    private val minioService: MinioService,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val redisService: RedisService,
) {

    @Transactional
    fun basicSignup(emailSignupRequest: EmailSignupRequest): EmailAuthResponse {
        if (memberRepository.existsByEmail(Email.from(emailSignupRequest.email))) {
            throw IllegalStateException("이메일: ${emailSignupRequest.email} 중복 오류")
        }
        val encodedPassword: String = passwordEncoder.encode(emailSignupRequest.password)
        val preSignedUrl: String = minioService.upload(emailSignupRequest.profileImage, emailSignupRequest.nickname)
        val member: Member = Member.toEntity(emailSignupRequest, encodedPassword, preSignedUrl)
        val savedMember: Member = memberRepository.save(member)

        val accessToken: String = jwtUtil.generateAccessToken(savedMember.id)
        redisService.set(savedMember.id.toString(), accessToken, Duration.ofMinutes(10))

        val refreshToken: String = jwtUtil.generateRefreshToken(savedMember.id)
        savedMember.refreshToken = refreshToken

        return Member.toDto(savedMember, accessToken, refreshToken)
    }

    @Transactional
    fun basicLogin(emailLoginRequest: EmailLoginRequest): EmailAuthResponse {
        val member: Member = memberRepository.findByEmail(Email.from(emailLoginRequest.email))
            ?: throw IllegalArgumentException("이메일: ${emailLoginRequest.email} 조회 오류")

        if (isValidPassword(emailLoginRequest.password, member.password)) {
            val accessToken: String = jwtUtil.generateAccessToken(member.id)
            redisService.set(member.id.toString(), accessToken, Duration.ofMinutes(10))

            val refreshToken: String = jwtUtil.generateRefreshToken(member.id)
            member.refreshToken = refreshToken

            return Member.toDto(member, accessToken, refreshToken)
        } else {
            throw IllegalArgumentException("비밀번호 검증 오류")
        }
    }

    @Transactional
    fun refresh(refreshRequest: RefreshRequest): RefreshResponse {
        val refreshToken: String = refreshRequest.refreshToken
        val member: Member = memberRepository.findByRefreshToken(refreshToken)
            ?: throw IllegalArgumentException("Refresh Token 조회 오류")

        jwtUtil.validateMemberIdInRefreshToken(refreshToken, member.id)

        val newAccessToken: String = jwtUtil.generateAccessToken(member.id)
        redisService.set(member.id.toString(), newAccessToken, Duration.ofMinutes(10))

        val newRefreshToken: String = jwtUtil.generateRefreshToken(member.id)
        member.refreshToken = newRefreshToken

        return RefreshResponse(
            id = member.id,
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
        )
    }

    private fun isValidPassword(input: String, real: String): Boolean {
        return passwordEncoder.matches(input, real)
    }
}
