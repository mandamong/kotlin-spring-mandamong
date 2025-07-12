package com.mandamong.server.infrastructure.gemini

import com.google.genai.Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GeminiConfig {

    @Bean
    fun client(): Client = Client()

}
