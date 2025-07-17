package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.entity.Action
import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.entity.Objective
import com.mandamong.server.mandalart.entity.Subject

data class MandalartDataResponse(
    val mandalart: Pair,
    val subject: Pair,
    val objectives: List<Pair>,
    val actions: List<List<Pair>>,
) {

    companion object {
        fun of(
            mandalart: Mandalart,
            subject: Subject,
            objectives: List<Objective>,
            actions: List<List<Action>>,
        ): MandalartDataResponse {
            return MandalartDataResponse(
                mandalart = Pair.of(mandalart.id, mandalart.name),
                subject = Pair.of(subject.id, subject.subject),
                objectives = objectives.map { Pair.of(it.id, it.objective) },
                actions = actions.map { action -> action.map { Pair.of(it.id, it.action) } }
            )
        }
    }

}
