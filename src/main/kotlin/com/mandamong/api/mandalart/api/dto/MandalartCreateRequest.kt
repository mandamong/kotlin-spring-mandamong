package com.mandamong.api.mandalart.api.dto

data class MandalartCreateRequest(
    val subject: String,
    val objectives: List<String>,
    val actions: List<String>,
)
