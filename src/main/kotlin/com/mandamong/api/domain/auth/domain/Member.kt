package com.mandamong.api.domain.auth.domain

import com.mandamong.api.domain.auth.api.dto.EmailAuthResponse
import com.mandamong.api.domain.auth.api.dto.EmailSignupRequest
import com.mandamong.api.global.entity.BaseTimeEntity
import com.mandamong.api.domain.model.Email
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "members")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0L,

    @Embedded
    var email: Email,

    @Column(name = "phone_number", nullable = true)
    var phoneNumber: String?,

    @Column(name = "nickname")
    var nickname: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "profile_image", columnDefinition = "TEXT")
    var profileImage: String,

    @Column(name = "language")
    var language: String,

    @Column(name = "refresh_token")
    var refreshToken: String? = null,

    @Column(name = "oauth_provider_uid")
    var oauthProviderUid: String? = null,
) : BaseTimeEntity() {

    companion object {
        fun toEntity(emailSignupRequest: EmailSignupRequest, profileImageUrl: String): Member {
            return Member(
                email = Email.from(emailSignupRequest.email),
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
                email = member.email.value,
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
