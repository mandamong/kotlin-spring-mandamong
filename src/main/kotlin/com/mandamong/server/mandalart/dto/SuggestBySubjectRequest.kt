package com.mandamong.server.mandalart.dto

import com.fasterxml.jackson.annotation.JsonAlias

data class SuggestBySubjectRequest(
    @JsonAlias(value = ["subject"])
    val question: String,
)
