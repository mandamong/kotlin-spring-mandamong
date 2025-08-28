package com.mandamong.server.infrastructure.llm.config

import com.mandamong.server.infrastructure.llm.properties.MandalartAgentProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(MandalartAgentProperties::class)
class LlmAgentConfig(
    private val properties: MandalartAgentProperties
) {

    @Bean
    fun webClient(): WebClient = WebClient.builder()
        .baseUrl(properties.url)
        .build()
}