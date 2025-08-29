package com.mandamong.server.mandalart.dto

import com.fasterxml.jackson.databind.JsonNode

data class SuggestBySubjectResponse(
    val objectives: List<String>,
    val actions: List<List<String>>,
) {

    companion object {
        fun from(json: JsonNode): SuggestBySubjectResponse {
            return SuggestBySubjectResponse(
                objectives = json.get(OBJECTIVES_KEY).map { it.asText() },
                actions = json.get(ACTIONS_KEY).map { arrayNode -> arrayNode.map { it.asText() } }
            )
        }

        private const val OBJECTIVES_KEY = "objectives"
        private const val ACTIONS_KEY = "actions"
    }

}
