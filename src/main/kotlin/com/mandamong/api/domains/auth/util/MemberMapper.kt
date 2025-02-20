package com.mandamong.api.domains.auth.util

import com.mandamong.api.domains.auth.api.dto.EmailSignupRequest
import com.mandamong.api.domains.auth.api.dto.EmailAuthResponse
import com.mandamong.api.domains.auth.domain.Member
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
class MemberMapper {
    companion object {
        fun toEntity(emailSignupRequest: EmailSignupRequest, profileImageUrl: String): Member {
            return Member(
                email = emailSignupRequest.email,
                password = emailSignupRequest.password,
                nickname = emailSignupRequest.nickname,
                profileImage = profileImageUrl,
                language = emailSignupRequest.language,
                phoneNumber = null,
            )
        }

        fun toDto(member: Member, accessToken: String, refreshToken: String): EmailAuthResponse {
            return EmailAuthResponse(
                id = member.id,
                email = member.email,
                nickname = member.nickname,
                profileImage = member.profileImage,
                language = member.language,
                accessToken = accessToken,
                refreshToken = refreshToken,
                time = LocalDateTime.now(),
            )
        }
    }
}
