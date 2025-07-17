package com.mandamong.server.mandalart.service

import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.mandalart.dto.MandalartUpdateRequest
import com.mandamong.server.mandalart.entity.Objective
import com.mandamong.server.mandalart.entity.Subject
import com.mandamong.server.mandalart.repository.ObjectiveRepository
import kotlin.jvm.optionals.getOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ObjectiveService(
    private val repository: ObjectiveRepository,
) {

    @Transactional
    fun create(objectives: List<String>, subject: Subject): List<Objective> {
        return objectives.map {
            val objective = Objective.of(it, subject)
            repository.save(objective)
        }
    }

    @Transactional
    fun update(id: Long, request: MandalartUpdateRequest): MandalartUpdateRequest {
        val objective = getById(id)
        objective.objective = request.updated
        return MandalartUpdateRequest(updated = objective.objective)
    }

    @Transactional
    fun findById(id: Long): Objective? = repository.findById(id).getOrNull()

    @Transactional
    fun getById(id: Long): Objective = findById(id) ?: throw IdNotFoundException(id)

    @Transactional
    fun findBySubjectId(subjectId: Long): List<Objective>? = repository.findBySubjectId(subjectId)

    @Transactional
    fun getBySubjectId(subjectId: Long): List<Objective> = findBySubjectId(subjectId)
        ?: throw IdNotFoundException(subjectId)

}
