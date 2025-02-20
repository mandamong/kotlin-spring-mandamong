package com.mandamong.api.domains.auth.api.dto

import org.springframework.web.multipart.MultipartFile

class EmailSignupRequest(
    val email: String,
    var password: String,
    val nickname: String,
    val profileImage: MultipartFile,
    val language: String,
)
