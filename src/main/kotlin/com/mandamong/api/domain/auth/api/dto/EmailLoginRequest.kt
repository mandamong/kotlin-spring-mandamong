package com.mandamong.api.domain.auth.api.dto

data class EmailLoginRequest(
    val email: String,
    val password: String,
)
