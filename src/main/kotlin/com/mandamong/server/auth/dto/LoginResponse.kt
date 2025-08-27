package com.mandamong.server.auth.dto

import com.mandamong.server.user.entity.User

data class LoginResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val image: String,
    val language: String,
    val accessToken: String,
    val refreshToken: String,
) {

    companion object {
        fun of(
            user: User,
            presignedUrl: String,
            accessToken: String,
            refreshToken: String,
        ): LoginResponse = LoginResponse(
            id = user.id,
            email = user.email.value,
            nickname = user.nickname,
            image = presignedUrl,
            language = user.language,
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

}
