package com.mandamong.server.auth.dto

data class LoginRequest(
    val email: String,
    val password: String,
)
