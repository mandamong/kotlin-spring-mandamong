package com.mandamong.server.mandalart.dto

data class MandalartDataResponse(
    val mandalart: Pair,
    val subject: Pair,
    val objectives: List<Pair>,
    val actions: List<List<Pair>>,
)
