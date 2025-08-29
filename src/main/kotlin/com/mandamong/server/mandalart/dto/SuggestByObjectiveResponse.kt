package com.mandamong.server.mandalart.dto

import com.fasterxml.jackson.databind.JsonNode

data class SuggestByObjectiveResponse(
    val actions: List<String>,
) {

    companion object {
        fun from(json: JsonNode): SuggestByObjectiveResponse {
            return SuggestByObjectiveResponse(
                actions = json.get(ACTIONS_KEY).map { it.asText() }
            )
        }

        private const val ACTIONS_KEY = "actions"
    }

}
