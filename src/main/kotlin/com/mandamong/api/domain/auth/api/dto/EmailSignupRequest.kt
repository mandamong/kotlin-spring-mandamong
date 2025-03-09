package com.mandamong.api.domain.auth.api.dto

import org.springframework.web.multipart.MultipartFile

data class EmailSignupRequest(
    val email: String,
    var password: String,
    val nickname: String,
    val profileImage: MultipartFile,
    val language: String,
)
