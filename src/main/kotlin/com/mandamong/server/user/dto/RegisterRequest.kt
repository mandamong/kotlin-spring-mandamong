package com.mandamong.server.user.dto

import com.mandamong.server.user.entity.Email
import com.mandamong.server.user.entity.User
import org.springframework.web.multipart.MultipartFile

data class RegisterRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val image: MultipartFile,
    val language: String,
) {

    companion object {
        fun toEntity(
            registerRequest: RegisterRequest,
            encodedPassword: String,
            image: String,
        ): User {
            return User(
                email = Email.from(registerRequest.email),
                password = encodedPassword,
                nickname = registerRequest.nickname,
                image = image,
                language = registerRequest.language,
                phoneNumber = null,
            )
        }
    }

}
