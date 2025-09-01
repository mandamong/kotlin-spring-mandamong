package com.mandamong.server.mandalart.service

import com.mandamong.server.common.annotation.lock.DistributedLock
import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.mandalart.dto.UpdateActionResponse
import com.mandamong.server.mandalart.entity.Action
import com.mandamong.server.mandalart.entity.Objective
import com.mandamong.server.mandalart.enums.Status
import com.mandamong.server.mandalart.event.dto.UpdateActionEvent
import com.mandamong.server.mandalart.repository.ActionRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ActionService(
    private val repository: ActionRepository,
    private val publisher: ApplicationEventPublisher,
) {
    @Transactional
    fun create(
        actions: List<List<String>>,
        objectives: List<Objective>,
    ): List<List<Action>> =
        actions.zip(objectives).map { (actionsByObjective, objective) ->
            actionsByObjective.map { action ->
                repository.save(Action.of(action, objective))
            }
        }

    @Transactional
    @DistributedLock(name = "ACTION", key = "#id")
    fun update(
        id: Long,
        newAction: String?,
        status: Status?,
    ): UpdateActionResponse {
        val action = getByIdWithAllData(id)
        newAction?.let { action.action = it }
        status?.let { newStatus ->
            action.status = newStatus
            val objective = action.objective
            val isObjectiveDone = objective.actions.all { it.status == Status.DONE }
            objective.status = if (isObjectiveDone) Status.DONE else Status.IN_PROGRESS
            val subject = objective.subject
            val isSubjectDone = subject.objectives.all { it.status == Status.DONE }
            subject.status = if (isSubjectDone) Status.DONE else Status.IN_PROGRESS
            val mandalart = subject.mandalart
            mandalart.status = subject.status
        }

        publisher.publishEvent(
            UpdateActionEvent(
                mandalartId = action.objective.subject.mandalart.id,
                actionId = action.id,
            ),
        )

        return UpdateActionResponse.of(action)
    }

    @Transactional(readOnly = true)
    fun findByIdWithAllData(id: Long): Action? = repository.findByIdWithAllData(id)

    @Transactional(readOnly = true)
    fun getByIdWithAllData(id: Long): Action = findByIdWithAllData(id) ?: throw IdNotFoundException(id)
}
