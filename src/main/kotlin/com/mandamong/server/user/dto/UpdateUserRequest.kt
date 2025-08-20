package com.mandamong.server.user.dto

import org.springframework.web.multipart.MultipartFile

data class UpdateUserRequest(
    val nickname: String? = null,
    val password: String? = null,
    val image: MultipartFile? = null,
)
