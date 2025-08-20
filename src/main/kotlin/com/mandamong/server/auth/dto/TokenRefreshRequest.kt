package com.mandamong.server.auth.dto

data class TokenRefreshRequest(
    val refreshToken: String,
)
