package com.mandamong.server.mandalart.dto.request

data class MandalartCreateRequest(
    val subject: String,
    val objectives: List<String>,
    val actions: List<String>,
)
