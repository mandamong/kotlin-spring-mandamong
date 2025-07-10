package com.mandamong.api.infrastructure.config

import com.google.genai.Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GeminiConfig {

    @Bean
    fun client(): Client {
        return Client()
    }
}
