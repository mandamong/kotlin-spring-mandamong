package com.mandamong.server.mandalart.facade

import com.mandamong.server.common.util.log.log
import com.mandamong.server.mandalart.dto.MandalartCreateRequest
import com.mandamong.server.mandalart.dto.MandalartDataResponse
import com.mandamong.server.mandalart.dto.MandalartUpdateRequest
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
        log().info("MANDALART_CREATED userId=${loginUser.userId}  mandalartId=${mandalart.id}")
        return MandalartDataResponse.of(mandalart, subject, objectives, actions)
    }

    fun update(id: Long, request: MandalartUpdateRequest, loginUser: AuthenticatedUser): MandalartUpdateRequest {
        val updated: String = mandalartService.update(id, request.updated)
        log().info("MANDALART_UPDATED userId=${loginUser.userId} mandalartId=$id")
        return MandalartUpdateRequest(updated = updated)
    }

    fun delete(id: Long, loginUser: AuthenticatedUser) {
        mandalartService.deleteById(id)
        log().info("MANDALART_DELETED userId=${loginUser.userId} mandalartId=$id")
    }

    fun getMandalartsByUserId(userId: Long): List<MandalartDataResponse> {
        val mandalarts = mandalartService.getByUserId(userId)
        val mandalartIds = mandalarts.joinToString(", ") { it.id.toString() }
        log().info("MANDALARTS userId=$userId mandalartIds=[$mandalartIds]")
        return mandalarts.map { mandalart ->
            val subject = subjectService.getByMandalartId(mandalart.id)
            val objectives = objectiveService.getBySubjectId(subject.id)
            val actions = actionService.getByObjectiveId(objectives)
            MandalartDataResponse.of(mandalart, subject, objectives, actions)
        }
    }

    fun getByMandalartId(id: Long, loginUser: AuthenticatedUser): MandalartDataResponse {
        val mandalart = mandalartService.getById(id)
        log().info("MANDALART userId=${loginUser.userId} mandalartId=$id")
        val subject = subjectService.getByMandalartId(id)
        val objectives = objectiveService.getBySubjectId(subject.id)
        val actions = actionService.getByObjectiveId(objectives)
        return MandalartDataResponse.of(mandalart, subject, objectives, actions)
    }

}
