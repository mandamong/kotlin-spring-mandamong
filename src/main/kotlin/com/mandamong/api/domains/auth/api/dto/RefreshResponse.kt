package com.mandamong.api.domains.auth.api.dto

import java.time.LocalDateTime

data class RefreshResponse (
    val id: Long,
    val accessToken: String,
    val refreshToken: String,
    val time: LocalDateTime = LocalDateTime.now(),
)
