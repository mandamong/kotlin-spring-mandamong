package com.mandamong.server.user.dto

import com.mandamong.server.user.entity.User
import com.mandamong.server.user.model.Email
import org.springframework.web.multipart.MultipartFile

data class CreateUserRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val image: MultipartFile?,
    val language: String,
) {

    fun toEntity(encodedPassword: String): User {
        return User(
            email = Email.from(email),
            password = encodedPassword,
            nickname = nickname,
            language = language,
        )
    }

}
