package com.mandamong.server.mandalart.dto

data class SuggestBySubjectResponse(
    val objectives: List<String>,
    val actions: List<List<String>>,
)
