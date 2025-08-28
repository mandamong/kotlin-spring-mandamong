package com.mandamong.server.infrastructure.llm.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("llm.agent")
data class MandalartAgentProperties(
    val url: String,
    val defaultKey: String,
    val projectId: ProjectId
) {
    data class ProjectId(
        val mandalart: Mandalart
    ) {
        data class Mandalart(
            val subject: String,
            val objective: String
        )
    }
}