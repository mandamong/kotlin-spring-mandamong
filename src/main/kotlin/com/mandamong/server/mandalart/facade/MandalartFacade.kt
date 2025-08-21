package com.mandamong.server.mandalart.facade

import com.mandamong.server.common.dto.PaginationParameter
import com.mandamong.server.common.dto.PaginationResponse
import com.mandamong.server.common.util.log.log
import com.mandamong.server.infrastructure.redis.CacheName
import com.mandamong.server.mandalart.dto.CreateMandalartRequest
import com.mandamong.server.mandalart.dto.ReadMandalartResponse
import com.mandamong.server.mandalart.dto.ReadMandalartsResponse
import com.mandamong.server.mandalart.dto.UpdateMandalartResponse
import com.mandamong.server.mandalart.service.ActionService
import com.mandamong.server.mandalart.service.MandalartService
import com.mandamong.server.mandalart.service.ObjectiveService
import com.mandamong.server.mandalart.service.SubjectService
import org.springframework.cache.annotation.Cacheable
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

    @Transactional
    fun create(request: CreateMandalartRequest, userId: Long): ReadMandalartResponse {
        val mandalart = mandalartService.create(request.name, userId)
        val subject = subjectService.create(request.subject, mandalart, userId)
        val objectives = objectiveService.create(request.objectives, subject)
        val actions = actionService.create(request.actions, objectives)
        log().info("MANDALART_CREATED userId=$userId  mandalartId=${mandalart.id}")
        return ReadMandalartResponse.of(mandalart, subject, objectives, actions)
    }

    @Transactional
    fun update(id: Long, newMandalartName: String, userId: Long): UpdateMandalartResponse {
        val mandalart = mandalartService.update(id, newMandalartName, userId)
        log().info("MANDALART_UPDATED userId=$userId mandalartId=$id")
        return UpdateMandalartResponse.of(mandalart)
    }

    @Transactional
    fun delete(id: Long, userId: Long) {
        mandalartService.deleteById(id, userId)
        log().info("MANDALART_DELETED userId=$userId mandalartId=$id")
    }

    @Transactional(readOnly = true)
    @Cacheable(
        cacheNames = [CacheName.MANDALARTS],
        key = "#userId",
        condition = "#paginationParameter.number <= 1"
    )
    fun getMandalartsByUserId(
        paginationParameter: PaginationParameter,
        userId: Long,
    ): PaginationResponse<ReadMandalartsResponse> {
        val mandalarts = mandalartService.getByUserIdWithPage(userId, paginationParameter)
        val mandalartIds = mandalarts.joinToString(", ") { it.id.toString() }
        val mandalartPage: Page<ReadMandalartsResponse> = mandalarts.map { ReadMandalartsResponse.of(it) }
        log().info("READ MANDALARTS userId=$userId mandalartIds=$mandalartIds")
        return PaginationResponse.of(mandalartPage)
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = [CacheName.MANDALART], key = "#id")
    fun getMandalartById(id: Long, userId: Long): ReadMandalartResponse {
        val mandalart = mandalartService.getByIdWithFullData(id)
        val subject = mandalart.subject!!
        val objectives = subject.objectives
        val actions = objectives.map { it.actions }
        log().info("READ MANDALART userId=$userId mandalartId=$id")
        return ReadMandalartResponse.of(mandalart, subject, objectives, actions)
    }

}
