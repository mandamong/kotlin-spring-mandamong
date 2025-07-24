package com.mandamong.server.user.entity

import com.mandamong.server.auth.dto.LoginResponse
import com.mandamong.server.common.entity.BaseTimeEntity
import com.mandamong.server.mandalart.entity.Mandalart
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
    var phoneNumber: String?,

    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "image", columnDefinition = "TEXT")
    var image: String,

    @Column(name = "language", nullable = false)
    var language: String,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE], orphanRemoval = true, fetch = FetchType.LAZY)
    private val mandalarts: List<Mandalart> = listOf()
) : BaseTimeEntity() {

    companion object {
        fun toDto(
            user: User,
            accessToken: String,
            refreshToken: String,
        ): LoginResponse {
            return LoginResponse(
                id = user.id,
                email = user.email.value,
                nickname = user.nickname,
                image = user.image,
                language = user.language,
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        }
    }

}
