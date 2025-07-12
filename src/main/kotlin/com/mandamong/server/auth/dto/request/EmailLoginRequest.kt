package com.mandamong.server.auth.dto.request

data class EmailLoginRequest(
    val email: String,
    val password: String,
)
