package com.mandamong.server.mandalart.facade

import com.mandamong.server.common.dto.PaginationParameter
import com.mandamong.server.common.dto.PaginationResponse
import com.mandamong.server.common.util.log.log
import com.mandamong.server.mandalart.dto.BasicData
import com.mandamong.server.mandalart.dto.MandalartCreateRequest
import com.mandamong.server.mandalart.dto.MandalartDataResponse
import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.service.ActionService
import com.mandamong.server.mandalart.service.MandalartService
import com.mandamong.server.mandalart.service.ObjectiveService
import com.mandamong.server.mandalart.service.SubjectService
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component

@Component
class MandalartFacade(
    private val mandalartService: MandalartService,
    private val subjectService: SubjectService,
    private val objectiveService: ObjectiveService,
    private val actionService: ActionService,
) {

    fun create(request: MandalartCreateRequest, userId: Long): MandalartDataResponse {
        val mandalart = mandalartService.create(request.name, userId)
        val subject = subjectService.create(request.subject, mandalart)
        val objectives = objectiveService.create(request.objectives, subject)
        val actions = actionService.create(request.actions, objectives)
        log().info("MANDALART_CREATED userId=$userId  mandalartId=${mandalart.id}")
        return MandalartDataResponse.of(mandalart, subject, objectives, actions)
    }

    fun update(id: Long, updated: String, userId: Long): BasicData {
        val mandalart = mandalartService.update(id, updated)
        log().info("MANDALART_UPDATED userId=$userId mandalartId=$id")
        return BasicData.of(mandalart.id, mandalart.name, mandalart.status)
    }

    fun delete(id: Long, userId: Long) {
        mandalartService.deleteById(id)
        log().info("MANDALART_DELETED userId=$userId mandalartId=$id")
    }

    fun getMandalartsByUserId(
        paginationParameter: PaginationParameter,
        userId: Long,
    ): PaginationResponse<MandalartDataResponse> {
        val mandalarts = mandalartService.getByUserIdWithPage(userId, paginationParameter)
        val mandalartIds = mandalarts.joinToString(", ") { it.id.toString() }
        val mandalartPage: Page<MandalartDataResponse> = mandalarts.map { createMandalartDataResponse(it) }
        log().info("READ MANDALARTS userId=$userId mandalartIds=$mandalartIds")
        return PaginationResponse.of(mandalartPage)
    }

    fun getMandalartById(id: Long, userId: Long): MandalartDataResponse {
        val mandalart = mandalartService.getByIdWithFullData(id)
        log().info("READ MANDALART userId=$userId mandalartId=$id")
        return createMandalartDataResponse(mandalart)
    }

    private fun createMandalartDataResponse(mandalart: Mandalart): MandalartDataResponse {
        val subject = mandalart.subject!!
        val objectives = subject.objectives
        val actions = objectives.map { it.actions }
        return MandalartDataResponse.of(mandalart, subject, objectives, actions)
    }

}
