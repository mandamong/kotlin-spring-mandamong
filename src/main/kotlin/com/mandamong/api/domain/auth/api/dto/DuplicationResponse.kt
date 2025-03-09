package com.mandamong.api.domain.auth.api.dto

import java.time.LocalDateTime

data class DuplicationResponse(
    val isDuplicated: Boolean,
    val time: LocalDateTime = LocalDateTime.now(),
)
