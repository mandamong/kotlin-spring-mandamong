package com.mandamong.server.auth.dto

data class TokenRefreshResponse(
    val id: Long,
    val accessToken: String,
    val refreshToken: String,
)
