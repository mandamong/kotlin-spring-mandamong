package com.mandamong.server.infrastructure.flowise.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "flowise")
data class FlowiseProperties(
    val baseUrl: String,
    val token: String,
    val projectId: ProjectId,
) {
    data class ProjectId(
        val subject: String,
        val objective: String,
    )
}
