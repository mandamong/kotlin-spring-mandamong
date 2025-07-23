package com.mandamong.server.mandalart.facade

import com.mandamong.server.common.request.PageParameter
import com.mandamong.server.common.response.PageResponse
import com.mandamong.server.common.util.log.log
import com.mandamong.server.mandalart.dto.ActionUpdateRequest
import com.mandamong.server.mandalart.dto.MandalartCreateRequest
import com.mandamong.server.mandalart.dto.MandalartDataResponse
import com.mandamong.server.mandalart.dto.MandalartUpdateRequest
import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.enums.Status
import com.mandamong.server.mandalart.service.ActionService
import com.mandamong.server.mandalart.service.MandalartService
import com.mandamong.server.mandalart.service.ObjectiveService
import com.mandamong.server.mandalart.service.SubjectService
import com.mandamong.server.user.dto.LoginUser
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MandalartFacade(
    private val mandalartService: MandalartService,
    private val subjectService: SubjectService,
    private val objectiveService: ObjectiveService,
    private val actionService: ActionService,
) {

    fun create(request: MandalartCreateRequest, loginUser: LoginUser): MandalartDataResponse {
        val mandalart = mandalartService.create(request.name, loginUser)
        val subject = subjectService.create(request.subject, mandalart)
        val objectives = objectiveService.create(request.objectives, subject)
        val actions = actionService.create(request.actions, objectives)
        log().info("MANDALART_CREATED userId=${loginUser.userId}  mandalartId=${mandalart.id}")
        return MandalartDataResponse.of(mandalart, subject, objectives, actions)
    }

    fun update(id: Long, request: MandalartUpdateRequest, loginUser: LoginUser): MandalartUpdateRequest {
        val mandalart = mandalartService.update(id, request.updated)
        log().info("MANDALART_UPDATED userId=${loginUser.userId} mandalartId=$id")
        return MandalartUpdateRequest(updated = mandalart.name)
    }

    @Transactional
    fun updateAction(id: Long, request: ActionUpdateRequest): ActionUpdateRequest {
        val action = actionService.getByIdWithAllData(id)
        val objective = action.objective
        val subject = objective.subject
        val mandalart = subject.mandalart

        request.updated?.let { action.action = it }
        request.status?.let {
            action.status = it
            val countInProgressActions = actionService.countByObjectiveIdAndStatus(objective.id, Status.IN_PROGRESS)
            val countInProgressObjectives = objectiveService.countBySubjectIdAndStatus(subject.id, Status.IN_PROGRESS)
            objective.status = if (countInProgressActions == 0) Status.DONE else Status.IN_PROGRESS
            subject.status = if (countInProgressObjectives == 0) Status.DONE else Status.IN_PROGRESS
            mandalart.status = subject.status
        }

        return ActionUpdateRequest.of(action)
    }

    fun delete(id: Long, loginUser: LoginUser) {
        mandalartService.deleteById(id)
        log().info("MANDALART_DELETED userId=${loginUser.userId} mandalartId=$id")
    }

    fun getMandalartsByUserId(pageParameter: PageParameter, loginUser: LoginUser): PageResponse<MandalartDataResponse> {
        val mandalarts = mandalartService.getByUserIdWithPage(loginUser.userId, pageParameter)
        val mandalartIds = mandalarts.joinToString(", ") { it.id.toString() }
        val mandalartPage: Page<MandalartDataResponse> = mandalarts.map { toMandalartDataResponse(it) }
        log().info("READ MANDALARTS userId=${loginUser.userId} mandalartIds=$mandalartIds")
        return PageResponse.of(mandalartPage)
    }

    fun getMandalartById(id: Long, loginUser: LoginUser): MandalartDataResponse {
        val mandalart = mandalartService.getByIdWithFullData(id)
        log().info("READ MANDALART userId=${loginUser.userId} mandalartId=$id")
        return toMandalartDataResponse(mandalart)
    }

    private fun toMandalartDataResponse(mandalart: Mandalart): MandalartDataResponse {
        val subject = mandalart.subject!!
        val objectives = subject.objectives
        val actions = objectives.map { it.actions }
        return MandalartDataResponse.of(mandalart, subject, objectives, actions)
    }

}
