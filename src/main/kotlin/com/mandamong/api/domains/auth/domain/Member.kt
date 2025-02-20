package com.mandamong.api.domains.auth.domain

import com.mandamong.api.domains.model.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "members")
class Member(
    @Column(name = "email")
    var email: String,

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
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L

    @Column(name = "refresh_token")
    var refreshToken: String? = null

    @Column(name = "oauth_provider_uid")
    var oauthProviderUid: String? = null
}
