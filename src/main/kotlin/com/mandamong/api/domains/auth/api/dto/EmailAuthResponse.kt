package com.mandamong.api.domains.auth.api.dto

import java.time.LocalDateTime

data class EmailAuthResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val profileImage: String,
    val language: String,
    val accessToken: String,
    val refreshToken: String,
    val time: LocalDateTime,
)
