package com.mandamong.server.user.dto

import com.mandamong.server.user.entity.Email
import com.mandamong.server.user.entity.User
import org.springframework.web.multipart.MultipartFile

data class EmailRegisterRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val profileImage: MultipartFile,
    val language: String,
) {

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
    }

}
