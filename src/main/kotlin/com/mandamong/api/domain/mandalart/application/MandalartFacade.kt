package com.mandamong.api.domain.mandalart.application

import com.mandamong.api.domain.mandalart.api.dto.MandalartCreateRequest
import com.mandamong.api.domain.mandalart.domain.Subject
import org.springframework.stereotype.Component

@Component
class MandalartFacade(
    private val subjectService: SubjectService,
    private val objectiveService: ObjectiveService,
    private val actionService: ActionService,
) {

    /*fun create(request: MandalartCreateRequest) {
        subjectService.save(Subject.from(request.subject))
        objectiveService.save(request.objectives)
        actionService.save(request.actions)
    }

    fun read(mandalartId: Long) {
        subjectService.findByMandalartId(mandalartId)
        objectiveService.findByMandalartId(mandalartId)
        actionService.findByMandalartId(mandalartId)
    }

    fun update(request: MandalartCreateRequest) {

    }

    fun delete(mandalartId: Long) {
        subjectService.delete(mandalartId)
        objectiveService.delete()
        actionService.delete()
    }*/
}
