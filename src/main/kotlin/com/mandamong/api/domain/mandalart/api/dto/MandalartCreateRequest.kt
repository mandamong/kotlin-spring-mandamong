package com.mandamong.api.domain.mandalart.api.dto

data class MandalartCreateRequest(
    val subject: String,
    val objectives: List<String>,
    val actions: List<String>,
)
