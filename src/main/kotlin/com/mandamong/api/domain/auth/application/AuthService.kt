package com.mandamong.api.domain.auth.application

import com.mandamong.api.domain.auth.api.dto.EmailAuthResponse
import com.mandamong.api.domain.auth.api.dto.EmailLoginRequest
import com.mandamong.api.domain.auth.api.dto.EmailSignupRequest
import com.mandamong.api.domain.auth.api.dto.RefreshRequest
import com.mandamong.api.domain.auth.api.dto.RefreshResponse
import com.mandamong.api.domain.auth.dao.MemberRepository
import com.mandamong.api.domain.auth.domain.Member
import com.mandamong.api.domain.auth.util.JwtUtil
import com.mandamong.api.infrastructure.application.MinioService
import com.mandamong.api.domain.model.Email
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val jwtUtil: JwtUtil,
    private val minioService: MinioService,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    @Transactional
    fun basicSignup(emailSignupRequest: EmailSignupRequest): EmailAuthResponse {
        if (memberRepository.existsByEmail(Email.from(emailSignupRequest.email))) {
            throw IllegalStateException("이메일: ${emailSignupRequest.email} 중복 오류")
        }

        emailSignupRequest.password = passwordEncoder.encode(emailSignupRequest.password)

        val preSignedUrl: String = minioService.upload(emailSignupRequest.profileImage, emailSignupRequest.nickname)

        val member: Member = Member.toEntity(emailSignupRequest, preSignedUrl)

        val savedMember: Member = memberRepository.save(member)

        val accessToken: String = jwtUtil.generateAccessToken(savedMember.id)
        val refreshToken: String = jwtUtil.generateRefreshToken(savedMember.id)
        savedMember.refreshToken = refreshToken

        return Member.toDto(savedMember, accessToken, refreshToken)
    }

    @Transactional
    fun basicLogin(emailLoginRequest: EmailLoginRequest): EmailAuthResponse {
        val member: Member = memberRepository.findByEmail(
            Email.from(emailLoginRequest.email),
        ) ?: throw IllegalArgumentException("이메일: ${emailLoginRequest.email} 조회 오류")

        if (isValidPassword(emailLoginRequest, member)) {
            val accessToken: String = jwtUtil.generateAccessToken(member.id)
            val refreshToken: String = jwtUtil.generateRefreshToken(member.id)


            return Member.toDto(member, accessToken, refreshToken)
        } else {
            throw IllegalArgumentException("잘못된 비밀번호입니다.")
        }
    }

    @Transactional
    fun refresh(refreshRequest: RefreshRequest): RefreshResponse {
        val refreshToken: String = refreshRequest.refreshToken

        val member: Member = memberRepository.findByRefreshToken(refreshToken)
            ?: throw IllegalArgumentException("Refresh Token 조회 오류")

        jwtUtil.validateRefreshToken(refreshToken, member.id)

        val accessToken: String = jwtUtil.generateAccessToken(member.id)
        member.refreshToken = jwtUtil.generateRefreshToken(member.id)

        return RefreshResponse(
            id = member.id,
            accessToken = accessToken,
            refreshToken = member.refreshToken!!,
        )
    }

    private fun isValidPassword(emailLoginRequest: EmailLoginRequest, member: Member): Boolean {
        return passwordEncoder.matches(emailLoginRequest.password, member.password)
    }
}
