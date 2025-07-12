package com.mandamong.server.mandalart.facade

import com.mandamong.server.mandalart.dto.request.MandalartCreateRequest
import com.mandamong.server.mandalart.service.ActionService
import com.mandamong.server.mandalart.service.ObjectiveService
import com.mandamong.server.mandalart.service.SubjectService
import org.springframework.stereotype.Component

@Component
class MandalartFacade(
    private val subjectService: SubjectService,
    private val objectiveService: ObjectiveService,
    private val actionService: ActionService,
) {

    fun create(request: MandalartCreateRequest) {

    }

    fun read(mandalartId: Long) {

    }

    fun update(request: MandalartCreateRequest) {

    }

    fun delete(mandalartId: Long) {

    }

}
