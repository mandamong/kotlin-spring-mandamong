package com.mandamong.server.infrastructure.flowise.config

import com.mandamong.server.infrastructure.flowise.properties.FlowiseProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
    private val properties: FlowiseProperties,
) {

    @Bean
    fun subjectClient(): WebClient {
        return createFlowiseClient(properties.projectId.subject)
    }

    @Bean
    fun objectiveClient(): WebClient {
        return createFlowiseClient(properties.projectId.objective)
    }

    private fun createFlowiseClient(projectId: String): WebClient {
        return WebClient.builder()
            .baseUrl("${properties.baseUrl}$PATH$projectId")
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("Authorization", "$TOKEN_PREFIX${properties.token}")
            .build()
    }

    companion object {
        private const val PATH = "/api/v1/prediction/"
        private const val TOKEN_PREFIX = "Bearer "
    }

}
