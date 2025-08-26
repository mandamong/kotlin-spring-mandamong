package com.mandamong.server.infrastructure.gemini.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource

@ConfigurationProperties(prefix = "schemas")
data class SchemaProperties(
    val subject: Resource,
    val objective: Resource,
)
