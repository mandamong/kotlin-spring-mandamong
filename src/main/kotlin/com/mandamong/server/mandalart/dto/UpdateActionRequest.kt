package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.entity.Action
import com.mandamong.server.mandalart.enums.Status

data class UpdateActionRequest(
    val newAction: String?,
    val status: Status?,
) {

    companion object {
        fun of(action: Action): UpdateActionRequest {
            return UpdateActionRequest(newAction = action.action, status = action.status)
        }
    }

}
