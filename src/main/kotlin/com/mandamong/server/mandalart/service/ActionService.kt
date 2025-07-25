package com.mandamong.server.mandalart.service

import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.mandalart.dto.ActionUpdateRequest
import com.mandamong.server.mandalart.dto.BasicData
import com.mandamong.server.mandalart.entity.Action
import com.mandamong.server.mandalart.entity.Objective
import com.mandamong.server.mandalart.enums.Status
import com.mandamong.server.mandalart.repository.ActionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ActionService(
    private val repository: ActionRepository,
) {

    @Transactional
    fun create(actions: List<List<String>>, objectives: List<Objective>): List<List<Action>> {
        return actions.zip(objectives).map { (actionsByObjective, objective) ->
            actionsByObjective.map { action ->
                repository.save(Action.of(action, objective))
            }
        }
    }

    @Transactional
    fun update(id: Long, request: ActionUpdateRequest): BasicData {
        val action = getByIdWithAllData(id)
        request.updated?.let { action.action = it }
        request.status?.let { status ->
            action.status = status
            val objective = action.objective
            val isObjectiveDone = objective.actions.all { it.status == Status.DONE }
            objective.status = if (isObjectiveDone) Status.DONE else Status.IN_PROGRESS
            val subject = objective.subject
            val isSubjectDone = subject.objectives.all { it.status == Status.DONE }
            subject.status = if (isSubjectDone) Status.DONE else Status.IN_PROGRESS
            val mandalart = subject.mandalart
            mandalart.status = subject.status
        }
        return BasicData.of(action.id, action.action, action.status)
    }

    @Transactional(readOnly = true)
    fun findByIdWithAllData(id: Long): Action? = repository.findByIdWithAllData(id)

    @Transactional(readOnly = true)
    fun getByIdWithAllData(id: Long): Action = findByIdWithAllData(id) ?: throw IdNotFoundException(id)

}
