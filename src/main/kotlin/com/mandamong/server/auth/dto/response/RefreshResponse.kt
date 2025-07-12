package com.mandamong.server.auth.dto.response

import java.time.LocalDateTime

data class RefreshResponse (
    val id: Long,
    val accessToken: String,
    val refreshToken: String,
    val time: LocalDateTime = LocalDateTime.now(),
)
