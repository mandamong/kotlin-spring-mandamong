package com.mandamong.api.domains.auth.application

import com.mandamong.api.domains.auth.api.dto.EmailLoginRequest
import com.mandamong.api.domains.auth.api.dto.EmailSignupRequest
import com.mandamong.api.domains.auth.api.dto.EmailAuthResponse
import com.mandamong.api.domains.auth.dao.MemberRepository
import com.mandamong.api.domains.auth.domain.Member
import com.mandamong.api.domains.auth.util.JwtUtil
import com.mandamong.api.domains.auth.util.MemberMapper
import com.mandamong.api.global.service.MinioService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val jwtUtil: JwtUtil,
    private val minioService: MinioService
) {
    private val encoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Transactional
    fun basicSignup(emailSignupRequest: EmailSignupRequest): EmailAuthResponse {
        if (memberRepository.existsByEmail(emailSignupRequest.email)) {
            throw IllegalStateException("이미 등록된 이메일입니다.")
        }

        emailSignupRequest.password = encoder.encode(emailSignupRequest.password)

        val preSignedUrl: String = minioService.upload(emailSignupRequest.profileImage, emailSignupRequest.nickname)

        val member: Member = MemberMapper.toEntity(emailSignupRequest, preSignedUrl)

        val savedMember: Member = memberRepository.save(member)

        val accessToken: String = jwtUtil.generateAccessToken(savedMember.id)
        val refreshToken: String = jwtUtil.generateRefreshToken(savedMember.id)
        savedMember.refreshToken = refreshToken

        return MemberMapper.toDto(savedMember, accessToken, refreshToken)
    }

    @Transactional
    fun basicLogin(emailLoginRequest: EmailLoginRequest): EmailAuthResponse {
        val member: Member = memberRepository.findByEmail(emailLoginRequest.email)

        if (isValidPassword(emailLoginRequest, member)) {
            val accessToken: String = jwtUtil.generateAccessToken(member.id)
            val refreshToken: String = jwtUtil.generateRefreshToken(member.id)
            return MemberMapper.toDto(member, accessToken, refreshToken)
        } else {
            throw IllegalArgumentException("잘못된 비밀번호입니다.")
        }
    }

    private fun isValidPassword(emailLoginRequest: EmailLoginRequest, member: Member): Boolean {
        return encoder.matches(emailLoginRequest.password, member.password)
    }
}
