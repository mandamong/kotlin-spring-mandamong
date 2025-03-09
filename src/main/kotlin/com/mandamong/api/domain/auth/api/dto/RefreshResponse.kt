package com.mandamong.api.domain.auth.api.dto

import java.time.LocalDateTime

data class RefreshResponse (
    val id: Long,
    val accessToken: String,
    val refreshToken: String,
    val time: LocalDateTime = LocalDateTime.now(),
)
