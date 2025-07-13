package com.mandamong.server.mandalart.facade

import com.mandamong.server.mandalart.dto.request.MandalartCreateRequest
import com.mandamong.server.mandalart.dto.response.MandalartDataResponse
import com.mandamong.server.mandalart.dto.response.Pair
import com.mandamong.server.mandalart.service.ActionService
import com.mandamong.server.mandalart.service.MandalartService
import com.mandamong.server.mandalart.service.ObjectiveService
import com.mandamong.server.mandalart.service.SubjectService
import com.mandamong.server.user.dto.AuthenticatedUser
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class MandalartFacade(
    private val mandalartService: MandalartService,
    private val subjectService: SubjectService,
    private val objectiveService: ObjectiveService,
    private val actionService: ActionService,
) {

    fun create(request: MandalartCreateRequest, user: AuthenticatedUser): ResponseEntity<MandalartDataResponse> {
        val mandalart = mandalartService.save(request.name, user)
        val subject = subjectService.save(request.subject, mandalart)
        val objectives = objectiveService.save(request.objectives, subject)
        val actions = actionService.save(request.actions, objectives)
        val response = MandalartDataResponse(
            Pair.of(mandalart.id, mandalart.name),
            Pair.of(subject.id, subject.subject),
            objectives.map { Pair.of(it.id, it.objective) },
            actions.map { action -> action.map { Pair.of(it.id, it.action) } }
        )
        return ResponseEntity.ok(response)
    }

    fun read(mandalartId: Long) {

    }

    fun delete(mandalartId: Long) {

    }

}
