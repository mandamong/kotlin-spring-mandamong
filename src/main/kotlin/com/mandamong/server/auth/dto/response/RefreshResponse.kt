package com.mandamong.server.auth.dto.response

data class RefreshResponse(
    val id: Long,
    val accessToken: String,
    val refreshToken: String,
)
