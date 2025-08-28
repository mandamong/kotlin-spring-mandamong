package com.mandamong.server.infrastructure.llm.dto

data class LlmSuggestionResponse<T>(
    val json: T,
    val question: String,
    val chatId: String,
    val chatMessageId: String,
    val isStreamValid: Boolean,
    val sessionId: String
)

data class SubjectData(
    val objectives: List<String>,
    val actions: List<List<String>>,
)

data class ObjectiveData(
    val actions: List<String>
)
