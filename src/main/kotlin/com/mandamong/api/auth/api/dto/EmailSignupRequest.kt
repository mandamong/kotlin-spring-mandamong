package com.mandamong.api.auth.api.dto

import org.springframework.web.multipart.MultipartFile

data class EmailSignupRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val profileImage: MultipartFile,
    val language: String,
)
