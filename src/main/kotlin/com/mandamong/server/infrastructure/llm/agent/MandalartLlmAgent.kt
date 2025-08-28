package com.mandamong.server.infrastructure.llm.agent

import com.mandamong.server.infrastructure.llm.dto.LlmSuggestionResponse
import com.mandamong.server.infrastructure.llm.dto.ObjectiveData
import com.mandamong.server.infrastructure.llm.dto.ObjectiveSuggestionRequest
import com.mandamong.server.infrastructure.llm.dto.SubjectData
import com.mandamong.server.infrastructure.llm.dto.SubjectSuggestionRequest
import com.mandamong.server.infrastructure.llm.exception.LlmAgentException
import com.mandamong.server.infrastructure.llm.properties.MandalartAgentProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class MandalartLlmAgent(
    @field:Qualifier("llmAgentWebClient")
    private val llmAgentWebClient: WebClient,
    private val properties: MandalartAgentProperties
) {

    fun suggestSubject(request: SubjectSuggestionRequest): LlmSuggestionResponse<SubjectData> {
        return suggest(request, properties.projectId.mandalart.subject)
    }

    fun suggestObjective(request: ObjectiveSuggestionRequest): LlmSuggestionResponse<ObjectiveData> {
        return suggest(request, properties.projectId.mandalart.objective)
    }

    private inline fun <reified T> suggest(request: Any, projectId: String): LlmSuggestionResponse<T> {
        return llmAgentWebClient.post()
            .uri("/api/v1/prediction/$projectId")
            .header("Authorization", "Bearer ${properties.defaultKey}")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<LlmSuggestionResponse<T>>() {})
            .block()
            ?: throw LlmAgentException(
                message = "LLM Agent로부터 응답을 받을 수 없습니다. Project ID: $projectId",
                projectId = projectId
            )
    }

}