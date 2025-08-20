package com.mandamong.server.user.dto

import org.springframework.web.multipart.MultipartFile

data class UserUpdateRequest(
    val nickname: String? = null,
    val password: String? = null,
    val image: MultipartFile? = null,
)
