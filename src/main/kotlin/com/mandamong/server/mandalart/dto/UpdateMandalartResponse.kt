package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.enums.Status

data class UpdateMandalartResponse(
    val id: Long,
    val mandalartName: String,
    val status: Status,
) {

    companion object {
        fun of(mandalart: Mandalart): UpdateMandalartResponse {
            return UpdateMandalartResponse(
                id = mandalart.id,
                mandalartName = mandalart.name,
                status = mandalart.status,
            )
        }
    }

}
