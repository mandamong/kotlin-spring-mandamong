package com.mandamong.server.user.dto.response

import java.time.LocalDateTime

data class DuplicationResponse(
    val isDuplicated: Boolean,
    val time: LocalDateTime = LocalDateTime.now(),
)
