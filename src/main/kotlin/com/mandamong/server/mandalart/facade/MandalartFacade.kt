package com.mandamong.server.mandalart.facade

import com.mandamong.server.mandalart.dto.MandalartCreateRequest
import com.mandamong.server.mandalart.dto.MandalartDataResponse
import com.mandamong.server.mandalart.dto.MandalartUpdateRequest
import com.mandamong.server.mandalart.dto.Pair
import com.mandamong.server.mandalart.service.ActionService
import com.mandamong.server.mandalart.service.MandalartService
import com.mandamong.server.mandalart.service.ObjectiveService
import com.mandamong.server.mandalart.service.SubjectService
import com.mandamong.server.user.dto.AuthenticatedUser
import org.springframework.stereotype.Component

@Component
class MandalartFacade(
    private val mandalartService: MandalartService,
    private val subjectService: SubjectService,
    private val objectiveService: ObjectiveService,
    private val actionService: ActionService,
) {

    fun create(request: MandalartCreateRequest, loginUser: AuthenticatedUser): MandalartDataResponse {
        val mandalart = mandalartService.create(request.name, loginUser)
        val subject = subjectService.create(request.subject, mandalart)
        val objectives = objectiveService.create(request.objectives, subject)
        val actions = actionService.create(request.actions, objectives)
        return MandalartDataResponse(
            Pair.of(mandalart.id, mandalart.name),
            Pair.of(subject.id, subject.subject),
            objectives.map { Pair.of(it.id, it.objective) },
            actions.map { action -> action.map { Pair.of(it.id, it.action) } }
        )
    }

    fun update(id: Long, request: MandalartUpdateRequest): MandalartUpdateRequest {
        val updated: String = mandalartService.update(id, request.updated)
        return MandalartUpdateRequest(updated = updated)
    }

    fun delete(id: Long) = mandalartService.deleteById(id)

    fun getMandalartsByUserId(userId: Long): List<MandalartDataResponse> {
        val mandalarts = mandalartService.getByUserId(userId)
        return mandalarts.map { mandalart ->
            val subject = subjectService.getByMandalartId(mandalart.id)
            val objectives = objectiveService.getBySubjectId(subject.id)
            val actions = actionService.getByObjectiveId(objectives)
            MandalartDataResponse(
                Pair.of(mandalart.id, mandalart.name),
                Pair.of(subject.id, subject.subject),
                objectives.map { Pair.of(it.id, it.objective) },
                actions.map { action -> action.map { Pair.of(it.id, it.action) } }
            )
        }
    }

    fun getByMandalartId(id: Long): MandalartDataResponse {
        val mandalart = mandalartService.getById(id)
        val subject = subjectService.getByMandalartId(id)
        val objectives = objectiveService.getBySubjectId(subject.id)
        val actions = actionService.getByObjectiveId(objectives)
        return MandalartDataResponse(
            Pair.of(mandalart.id, mandalart.name),
            Pair.of(subject.id, subject.subject),
            objectives.map { Pair.of(it.id, it.objective) },
            actions.map { action -> action.map { Pair.of(it.id, it.action) } }
        )
    }

}
