package com.mandamong.server.infrastructure.llm

import com.mandamong.server.infrastructure.llm.dto.LlmSuggestionResponse
import com.mandamong.server.infrastructure.llm.dto.ObjectiveData
import com.mandamong.server.infrastructure.llm.dto.ObjectiveSuggestionRequest
import com.mandamong.server.infrastructure.llm.dto.SubjectData
import com.mandamong.server.infrastructure.llm.dto.SubjectSuggestionRequest
import com.mandamong.server.infrastructure.llm.properties.MandalartAgentProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class MandalartLlmAgent(
    private val webClient: WebClient,
    private val properties: MandalartAgentProperties
) {
    
    fun suggestSubject(request: SubjectSuggestionRequest): LlmSuggestionResponse<SubjectData> {
        return suggest(request, properties.projectId.mandalart.subject)
    }
    
    fun suggestObjective(request: ObjectiveSuggestionRequest): LlmSuggestionResponse<ObjectiveData> {
        return suggest(request, properties.projectId.mandalart.objective)
    }
    
    private inline fun <reified T> suggest(request: Any, projectId: String): LlmSuggestionResponse<T> {
        return webClient.post()
            .uri("/api/v1/prediction/$projectId")
            .header("Authorization", "Bearer ${properties.defaultKey}")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(object : org.springframework.core.ParameterizedTypeReference<LlmSuggestionResponse<T>>() {})
            .block()!!
    }
}