package com.mandamong.server.infrastructure.llm.exception

class LlmAgentException(
    message: String,
    val projectId: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)
