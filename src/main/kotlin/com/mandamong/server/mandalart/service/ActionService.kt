package com.mandamong.server.mandalart.service

import com.mandamong.server.common.error.exception.IdNotFoundException
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
    fun create(actions: List<List<String>>, objectives: List<Objective>): List<List<Action>> {
        return actions.zip(objectives).map { (actionsByObjective, objective) ->
            actionsByObjective.map { action ->
                repository.save(Action.of(action, objective))
            }
        }
    }

    @Transactional(readOnly = true)
    fun findByIdWithAllData(id: Long): Action? = repository.findByIdWithAllData(id)

    @Transactional(readOnly = true)
    fun getByIdWithAllData(id: Long): Action = findByIdWithAllData(id) ?: throw IdNotFoundException(id)

}
