package com.mandamong.api.auth.api.dto

data class EmailLoginRequest(
    val email: String,
    val password: String,
)
