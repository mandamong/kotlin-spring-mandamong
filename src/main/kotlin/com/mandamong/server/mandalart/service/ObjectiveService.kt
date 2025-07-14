package com.mandamong.server.mandalart.service

import com.mandamong.server.mandalart.dto.request.MandalartUpdateRequest
import com.mandamong.server.mandalart.entity.Objective
import com.mandamong.server.mandalart.entity.Subject
import com.mandamong.server.mandalart.repository.ObjectiveRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ObjectiveService(
    private val repository: ObjectiveRepository,
) {

    @Transactional
    fun save(objectives: List<String>, subject: Subject): List<Objective> {
        return objectives.map {
            val objective = Objective.of(it, subject)
            repository.save(objective)
        }
    }

    @Transactional
    fun findBySubjectId(subjectId: Long): List<Objective> {
        return repository.findBySubjectId(subjectId)
    }

    @Transactional
    fun deleteBySubjectId(subjectId: Long) {
        repository.deleteBySubjectId(subjectId)
    }

    @Transactional
    fun updateObjective(objectiveId: Long, request: MandalartUpdateRequest): MandalartUpdateRequest {
        val objective = repository.findById(objectiveId).orElseThrow()
        objective.objective = request.updated
        return MandalartUpdateRequest(updated = objective.objective)
    }

}
