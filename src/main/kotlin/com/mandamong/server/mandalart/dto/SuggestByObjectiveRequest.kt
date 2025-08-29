package com.mandamong.server.mandalart.dto

import com.fasterxml.jackson.annotation.JsonAlias

data class SuggestByObjectiveRequest(
    @JsonAlias(value = ["objective"])
    val question: String,
)
