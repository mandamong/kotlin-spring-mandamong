package com.mandamong.api.auth.api.dto

import java.time.LocalDateTime

data class DuplicationResponse(
    val isDuplicated: Boolean,
    val time: LocalDateTime = LocalDateTime.now(),
)
