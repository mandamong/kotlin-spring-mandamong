package com.mandamong.server.infrastructure.flowise.service

import com.mandamong.server.common.error.exception.base.BusinessBaseException
import com.mandamong.server.mandalart.dto.FlowiseResponse
import com.mandamong.server.mandalart.dto.SuggestByObjectiveRequest
import com.mandamong.server.mandalart.dto.SuggestByObjectiveResponse
import com.mandamong.server.mandalart.dto.SuggestBySubjectRequest
import com.mandamong.server.mandalart.dto.SuggestBySubjectResponse
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class FlowiseService(
    private val subjectClient: WebClient,
    private val objectiveClient: WebClient,
) {

    fun suggestBySubject(request: SuggestBySubjectRequest): SuggestBySubjectResponse {
        val flowiseResponse = subjectClient.post()
            .bodyValue(request)
            .retrieve()
            .bodyToMono(FlowiseResponse::class.java)
            .block()
            ?: throw BusinessBaseException()
        return SuggestBySubjectResponse.from(flowiseResponse.json)
    }

    fun suggestByObjective(request: SuggestByObjectiveRequest): SuggestByObjectiveResponse {
        val flowiseResponse = objectiveClient.post()
            .bodyValue(request)
            .retrieve()
            .bodyToMono(FlowiseResponse::class.java)
            .block()
            ?: throw BusinessBaseException()
        return SuggestByObjectiveResponse.from(flowiseResponse.json)
    }

}
