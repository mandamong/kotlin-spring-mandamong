package com.mandamong.server.mandalart.service

import com.mandamong.server.mandalart.dto.request.MandalartUpdateRequest
import com.mandamong.server.mandalart.entity.Action
import com.mandamong.server.mandalart.entity.Objective
import com.mandamong.server.mandalart.repository.ActionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ActionService(
    private val repository: ActionRepository,
) {

    @Transactional
    fun save(actions: List<List<String>>, objectives: List<Objective>): List<List<Action>> {
        return actions.zip(objectives).map { (actionList, objective) ->
            actionList.map { repository.save(Action.of(it, objective)) }
        }
    }

    @Transactional
    fun updateAction(actionId: Long, request: MandalartUpdateRequest): MandalartUpdateRequest {
        val action = repository.findById(actionId).orElseThrow()
        action.action = request.updated
        return MandalartUpdateRequest(updated = action.action)
    }

    @Transactional
    fun findByObjectiveId(objectives: List<Objective>): List<List<Action>> {
        return objectives.map { objective ->
            repository.findByObjectiveId(objective.id)
        }
    }

    @Transactional
    fun deleteByObjectiveId(objectives: List<Objective>) {
        objectives.map { objective ->
            repository.deleteByObjectiveId(objective.id)
        }
    }

}
