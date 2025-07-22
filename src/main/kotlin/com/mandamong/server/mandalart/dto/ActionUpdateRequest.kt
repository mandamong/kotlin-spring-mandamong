package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.enums.Status

data class ActionUpdateRequest(
    val updated: String?,
    val status: Status?,
)
