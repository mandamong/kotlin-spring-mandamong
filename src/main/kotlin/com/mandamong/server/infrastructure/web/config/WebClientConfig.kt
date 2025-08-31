package com.mandamong.server.infrastructure.flowise.config

import com.mandamong.server.infrastructure.flowise.properties.FlowiseProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
    private val properties: FlowiseProperties,
    @param:Value("\${discord.webhook.url}") private val discordWebhookUrl: String
) {

    @Bean
    fun subjectClient(): WebClient {
        return createFlowiseClient(properties.projectId.subject)
    }

    @Bean
    fun objectiveClient(): WebClient {
        return createFlowiseClient(properties.projectId.objective)
    }

    @Bean
    fun discordClient(): WebClient {
        return WebClient.builder()
            .baseUrl(discordWebhookUrl)
            .defaultHeaders {
                it.contentType = MediaType.APPLICATION_JSON
            }
            .build()
    }

    private fun createFlowiseClient(projectId: String): WebClient {
        return WebClient.builder()
            .baseUrl("${properties.baseUrl}$PATH$projectId")
            .defaultHeaders {
                it.contentType = MediaType.APPLICATION_JSON
                it.setBearerAuth(properties.token)
            }
            .build()
    }

    companion object {
        private const val PATH = "/api/v1/prediction/"
    }

}
