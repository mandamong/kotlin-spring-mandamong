package com.mandamong.server.mandalart.service

import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.mandalart.dto.UpdateObjectiveResponse
import com.mandamong.server.mandalart.entity.Objective
import com.mandamong.server.mandalart.entity.Subject
import com.mandamong.server.mandalart.event.dto.UpdateObjectiveEvent
import com.mandamong.server.mandalart.repository.ObjectiveRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class ObjectiveService(
    private val repository: ObjectiveRepository,
    private val publisher: ApplicationEventPublisher,
) {
    @Transactional
    fun create(
        objectives: List<String>,
        subject: Subject,
    ): List<Objective> =
        objectives.map {
            val objective = Objective.of(it, subject)
            repository.save(objective)
        }

    @Transactional
    fun update(
        id: Long,
        newObjective: String,
    ): UpdateObjectiveResponse {
        val objective = getById(id)
        objective.objective = newObjective

        publisher.publishEvent(
            UpdateObjectiveEvent(
                mandalartId = objective.subject.mandalart.id,
                objectiveId = objective.id,
            ),
        )

        return UpdateObjectiveResponse.of(objective)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Objective? = repository.findById(id).getOrNull()

    @Transactional(readOnly = true)
    fun getById(id: Long): Objective = findById(id) ?: throw IdNotFoundException(id)
}
