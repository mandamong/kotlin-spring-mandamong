package com.mandamong.server.mandalart.dto.request

data class MandalartCreateRequest(
    val name: String,
    val subject: String,
    val objectives: List<String>,
    val actions: List<List<String>>,
)
