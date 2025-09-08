package com.mandamong.server.infrastructure.flowise.service

import com.mandamong.server.common.error.exception.base.BusinessBaseException
import com.mandamong.server.mandalart.dto.FlowiseResponse
import com.mandamong.server.mandalart.dto.SuggestByObjectiveRequest
import com.mandamong.server.mandalart.dto.SuggestByObjectiveResponse
import com.mandamong.server.mandalart.dto.SuggestBySubjectRequest
import com.mandamong.server.mandalart.dto.SuggestBySubjectResponse
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class FlowiseService(
    private val subjectClient: WebClient,
    private val objectiveClient: WebClient,
) {
    fun suggestBySubject(request: SuggestBySubjectRequest): SuggestBySubjectResponse =
        sendRequest(subjectClient, request)

    fun suggestByObjective(request: SuggestByObjectiveRequest): SuggestByObjectiveResponse =
        sendRequest(objectiveClient, request)

    private inline fun <reified T> sendRequest(
        webClient: WebClient,
        request: Any,
    ): T =
        webClient
            .post()
            .bodyValue(request)
            .retrieve()
            .bodyToMono(typeReference<T>())
            .block()
            ?.json
            ?: throw BusinessBaseException()

    private inline fun <reified T> typeReference() = object : ParameterizedTypeReference<FlowiseResponse<T>>() {}
}
