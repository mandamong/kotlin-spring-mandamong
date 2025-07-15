package com.mandamong.server.user.entity

import com.mandamong.server.auth.dto.response.EmailLoginResponse
import com.mandamong.server.common.entity.BaseTimeEntity
import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.user.dto.request.EmailRegisterRequest
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

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

    @Column(name = "oauth_provider_uid")
    var oauthProviderUid: String? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    private val mandalarts: List<Mandalart> = listOf()
) : BaseTimeEntity() {

    companion object {
        fun toEntity(
            emailRegisterRequest: EmailRegisterRequest,
            encodedPassword: String,
            profileImageUrl: String,
        ): User {
            return User(
                email = Email.from(emailRegisterRequest.email),
                password = encodedPassword,
                nickname = emailRegisterRequest.nickname,
                profileImage = profileImageUrl,
                language = emailRegisterRequest.language,
                phoneNumber = null,
            )
        }

        fun toDto(
            user: User,
            accessToken: String,
            refreshToken: String,
        ): EmailLoginResponse {
            return EmailLoginResponse(
                id = user.id,
                email = user.email.value,
                nickname = user.nickname,
                profileImage = user.profileImage,
                language = user.language,
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        }
    }

}
