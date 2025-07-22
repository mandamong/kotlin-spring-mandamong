package com.mandamong.server.mandalart.service

import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.mandalart.entity.Action
import com.mandamong.server.mandalart.entity.Objective
import com.mandamong.server.mandalart.enums.Status
import com.mandamong.server.mandalart.repository.ActionRepository
import kotlin.jvm.optionals.getOrNull
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

    @Transactional(readOnly = true)
    fun findById(id: Long): Action? = repository.findById(id).getOrNull()

    @Transactional(readOnly = true)
    fun getById(id: Long): Action = findById(id) ?: throw IdNotFoundException(id)

    @Transactional(readOnly = true)
    fun findByIdWithAllData(id: Long): Action? = repository.findByIdWithAllData(id)

    @Transactional(readOnly = true)
    fun getByIdWithAllData(id: Long): Action = findByIdWithAllData(id) ?: throw IdNotFoundException(id)

    @Transactional(readOnly = true)
    fun countByObjectiveIdAndStatus(objectiveId: Long, status: Status): Int =
        repository.countByObjectiveIdAndStatus(objectiveId, status)

}
