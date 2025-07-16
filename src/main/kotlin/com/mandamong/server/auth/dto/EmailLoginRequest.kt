package com.mandamong.server.auth.dto

data class EmailLoginRequest(
    val email: String,
    val password: String,
)
