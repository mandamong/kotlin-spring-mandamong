package com.mandamong.api.domains.auth.api.dto

import java.time.LocalDateTime

data class DuplicationResponse(
    val isDuplicated: Boolean,
    val time: LocalDateTime = LocalDateTime.now(),
)
