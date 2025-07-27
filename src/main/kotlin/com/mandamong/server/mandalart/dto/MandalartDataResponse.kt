package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.entity.Action
import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.entity.Objective
import com.mandamong.server.mandalart.entity.Subject

data class MandalartDataResponse(
    val mandalart: BasicData,
    val subject: BasicData,
    val objectives: List<BasicData>,
    val actions: List<List<BasicData>>,
) {

    companion object {
        fun of(
            mandalart: Mandalart,
            subject: Subject,
            objectives: List<Objective>,
            actions: List<List<Action>>,
        ): MandalartDataResponse {
            return MandalartDataResponse(
                mandalart = BasicData.of(mandalart.id, mandalart.name, mandalart.status),
                subject = BasicData.of(subject.id, subject.subject, subject.status),
                objectives = objectives.map { BasicData.of(it.id, it.objective, it.status) },
                actions = actions.map { action -> action.map { BasicData(it.id, it.action, it.status) } }
            )
        }
    }

}
