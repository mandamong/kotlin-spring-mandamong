package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.entity.Action
import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.entity.Objective
import com.mandamong.server.mandalart.entity.Subject

open class ReadMandalartResponse(
    val mandalart: UpdateMandalartResponse,
    val subject: UpdateSubjectResponse,
    val objectives: List<UpdateObjectiveResponse>,
    val actions: List<List<UpdateActionResponse>>,
) {

    companion object {
        fun of(
            mandalart: Mandalart,
            subject: Subject,
            objectives: List<Objective>,
            actions: List<List<Action>>,
        ): ReadMandalartResponse {
            return ReadMandalartResponse(
                mandalart = UpdateMandalartResponse.of(mandalart),
                subject = UpdateSubjectResponse.of(subject),
                objectives = objectives.map { UpdateObjectiveResponse.of(it) },
                actions = actions.map { action -> action.map { UpdateActionResponse.of(it) } }
            )
        }
    }

}
