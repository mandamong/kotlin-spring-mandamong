package com.mandamong.server.user.dto.request

import org.springframework.web.multipart.MultipartFile

data class EmailRegisterRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val profileImage: MultipartFile,
    val language: String,
)
