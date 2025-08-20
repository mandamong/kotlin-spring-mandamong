package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.entity.Objective
import com.mandamong.server.mandalart.enums.Status

data class UpdateObjectiveResponse(
    val id: Long,
    val objective: String,
    val status: Status,
) {

    companion object {
        fun of(objective: Objective): UpdateObjectiveResponse {
            return UpdateObjectiveResponse(
                id = objective.id,
                objective = objective.objective,
                status = objective.status,
            )
        }
    }

}
