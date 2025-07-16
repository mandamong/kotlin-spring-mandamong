package com.mandamong.server.auth.dto

data class RefreshResponse(
    val id: Long,
    val accessToken: String,
    val refreshToken: String,
)
