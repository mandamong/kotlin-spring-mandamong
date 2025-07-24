package com.mandamong.server.auth.dto

data class LoginResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val image: String,
    val language: String,
    val accessToken: String,
    val refreshToken: String,
)
