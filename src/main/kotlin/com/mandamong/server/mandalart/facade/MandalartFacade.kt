package com.mandamong.server.mandalart.facade

import com.mandamong.server.common.util.log.log
import com.mandamong.server.mandalart.dto.ActionUpdateRequest
import com.mandamong.server.mandalart.dto.MandalartCreateRequest
import com.mandamong.server.mandalart.dto.MandalartDataResponse
import com.mandamong.server.mandalart.dto.MandalartUpdateRequest
import com.mandamong.server.mandalart.enums.Status
import com.mandamong.server.mandalart.service.ActionService
import com.mandamong.server.mandalart.service.MandalartService
import com.mandamong.server.mandalart.service.ObjectiveService
import com.mandamong.server.mandalart.service.SubjectService
import com.mandamong.server.user.dto.LoginUser
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
            val inProgressCountInObjective = actionService.countByObjectiveIdAndStatus(objective.id, Status.IN_PROGRESS)
            when (inProgressCountInObjective) {
                0 -> objective.status = Status.DONE
                else -> objective.status = Status.IN_PROGRESS
            }

            val inProgressCountInSubject = objectiveService.countBySubjectIdAndStatus(subject.id, Status.IN_PROGRESS)
            when (inProgressCountInSubject) {
                0 -> {
                    subject.status = Status.DONE
                    mandalart.status = Status.DONE
                }
                else -> {
                    subject.status = Status.IN_PROGRESS
                    mandalart.status = Status.IN_PROGRESS
                }
            }
        }

        return ActionUpdateRequest(updated = action.action, status = action.status)
    }

    fun delete(id: Long, loginUser: LoginUser) {
        mandalartService.deleteById(id)
        log().info("MANDALART_DELETED userId=${loginUser.userId} mandalartId=$id")
    }

    fun getMandalartsByUserId(userId: Long): List<MandalartDataResponse> {
        val mandalarts = mandalartService.getByUserIdWithFullData(userId)
        val mandalartIds = mandalarts.joinToString(", ") { it.id.toString() }
        log().info("READ MANDALARTS userId=$userId mandalartIds=$mandalartIds")
        return mandalarts.map { mandalart ->
            val subject = mandalart.subject!!
            val objectives = subject.objectives
            val actions = objectives.map { it.actions }
            MandalartDataResponse.of(mandalart, subject, objectives, actions)
        }
    }

    fun getMandalartById(id: Long, loginUser: LoginUser): MandalartDataResponse {
        val mandalart = mandalartService.getByIdWithFullData(id)
        val subject = mandalart.subject!!
        val objectives = subject.objectives
        val actions = objectives.map { it.actions }
        log().info("READ MANDALART userId=${loginUser.userId} mandalartId=$id")
        return MandalartDataResponse.of(mandalart, subject, objectives, actions)
    }

}
