package com.mandamong.server.mandalart.facade

import com.mandamong.server.mandalart.dto.request.MandalartCreateRequest
import com.mandamong.server.mandalart.dto.request.MandalartUpdateRequest
import com.mandamong.server.mandalart.dto.response.MandalartDataResponse
import com.mandamong.server.mandalart.dto.response.Pair
import com.mandamong.server.mandalart.service.ActionService
import com.mandamong.server.mandalart.service.MandalartService
import com.mandamong.server.mandalart.service.ObjectiveService
import com.mandamong.server.mandalart.service.SubjectService
import com.mandamong.server.user.dto.AuthenticatedUser
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MandalartFacade(
    private val mandalartService: MandalartService,
    private val subjectService: SubjectService,
    private val objectiveService: ObjectiveService,
    private val actionService: ActionService,
) {

    fun create(request: MandalartCreateRequest, user: AuthenticatedUser): MandalartDataResponse {
        val mandalart = mandalartService.save(request.name, user)
        val subject = subjectService.save(request.subject, mandalart)
        val objectives = objectiveService.save(request.objectives, subject)
        val actions = actionService.save(request.actions, objectives)
        return MandalartDataResponse(
            Pair.of(mandalart.id, mandalart.name),
            Pair.of(subject.id, subject.subject),
            objectives.map { Pair.of(it.id, it.objective) },
            actions.map { action -> action.map { Pair.of(it.id, it.action) } }
        )
    }

    fun getMandalarts(user: AuthenticatedUser): List<MandalartDataResponse> {
        val mandalarts = mandalartService.getMandalarts(user.userId)
        return mandalarts.map { mandalart ->
            val subject = subjectService.findByMandalartId(mandalart.id)
            val objectives = objectiveService.findBySubjectId(subject.id)
            val actions = actionService.findByObjectiveId(objectives)
            MandalartDataResponse(
                Pair.of(mandalart.id, mandalart.name),
                Pair.of(subject.id, subject.subject),
                objectives.map { Pair.of(it.id, it.objective) },
                actions.map { action -> action.map { Pair.of(it.id, it.action) } }
            )
        }
    }

    fun getMandalart(mandalartId: Long): MandalartDataResponse {
        val mandalart = mandalartService.findById(mandalartId)
        val subject = subjectService.findByMandalartId(mandalartId)
        val objectives = objectiveService.findBySubjectId(subject.id)
        val actions = actionService.findByObjectiveId(objectives)
        return MandalartDataResponse(
            Pair.of(mandalart.id, mandalart.name),
            Pair.of(subject.id, subject.subject),
            objectives.map { Pair.of(it.id, it.objective) },
            actions.map { action -> action.map { Pair.of(it.id, it.action) } }
        )
    }

    fun delete(mandalartId: Long) {
        val subject = subjectService.findByMandalartId(mandalartId)
        val objectives = objectiveService.findBySubjectId(subject.id)

        actionService.deleteByObjectiveId(objectives)
        objectiveService.deleteBySubjectId(subject.id)
        subjectService.deleteByMandalartId(mandalartId)
        mandalartService.deleteById(mandalartId)
    }

    @Transactional
    fun updateName(mandalartId: Long, request: MandalartUpdateRequest): MandalartUpdateRequest {
        val mandalart = mandalartService.findById(mandalartId)
        mandalart.name = request.updated
        return MandalartUpdateRequest(updated = mandalart.name)
    }

}
