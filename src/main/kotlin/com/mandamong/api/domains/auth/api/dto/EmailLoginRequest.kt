package com.mandamong.api.domains.auth.api.dto

data class EmailLoginRequest(
    val email: String,
    val password: String,
)
