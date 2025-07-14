package com.mandamong.server.auth.dto.response

data class EmailLoginResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val profileImage: String,
    val language: String,
    val accessToken: String,
    val refreshToken: String,
)
