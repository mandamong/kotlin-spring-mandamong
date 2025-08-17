package com.mandamong.server.user.entity

import com.mandamong.server.auth.dto.LoginResponse
import com.mandamong.server.common.entity.BaseTimeEntity
import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.user.model.Email
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
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
    val id: Long = 0L,

    @Embedded
    var email: Email,

    @Column(name = "phone_number", nullable = true)
    var phoneNumber: String? = null,

    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Column(name = "image_object_key", nullable = false)
    var imageKey: String = "/user/profile/default/default.png",

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "language", nullable = false)
    var language: String,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE], orphanRemoval = true, fetch = FetchType.LAZY)
    val mandalarts: List<Mandalart> = listOf()
) : BaseTimeEntity() {

    fun toDto(
        presignedUrl: String,
        accessToken: String,
        refreshToken: String,
    ): LoginResponse {
        return LoginResponse(
            id = id,
            email = email.value,
            nickname = nickname,
            image = presignedUrl,
            language = language,
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

}
