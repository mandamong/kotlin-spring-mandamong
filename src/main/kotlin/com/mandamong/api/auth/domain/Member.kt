package com.mandamong.api.auth.domain

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

    @Column(name = "phone_number")
    var phoneNumber: String,

    @Column(name = "nickname")
    var nickname: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "profile_image")
    var profileImage: String,

    @Column(name = "language")
    var language: String,

    @Column(name = "refresh_token")
    var refresh_token: String,

    @Column(name = "oauth_provider_uid")
    var oauthProviderUid: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}
