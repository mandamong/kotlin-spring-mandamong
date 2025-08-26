package com.mandamong.server.infrastructure.gemini.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource

@ConfigurationProperties(prefix = "schemas")
class SchemaProperties {
    lateinit var subject: Resource
    lateinit var objective: Resource
}