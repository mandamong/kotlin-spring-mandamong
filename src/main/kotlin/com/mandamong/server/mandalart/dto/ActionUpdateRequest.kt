package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.entity.Action
import com.mandamong.server.mandalart.enums.Status

data class ActionUpdateRequest(
    val updated: String?,
    val status: Status?,
) {

    companion object {
        fun of(action: Action): ActionUpdateRequest =
            ActionUpdateRequest(updated = action.action, status = action.status)
    }

}
