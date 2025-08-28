package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.dto.ApiResponse
import com.mandamong.server.infrastructure.llm.MandalartLlmAgent
import com.mandamong.server.infrastructure.llm.dto.ObjectiveSuggestionRequest
import com.mandamong.server.mandalart.dto.SuggestByObjectiveRequest
import com.mandamong.server.mandalart.dto.SuggestByObjectiveResponse
import com.mandamong.server.mandalart.dto.UpdateObjectiveRequest
import com.mandamong.server.mandalart.dto.UpdateObjectiveResponse
import com.mandamong.server.mandalart.service.ObjectiveService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ObjectiveController(
    private val service: ObjectiveService,
    private val mandalartLlmAgent: MandalartLlmAgent,
) {

    @PatchMapping(ApiPath.Objective.UPDATE)
    fun update(
        @PathVariable objectiveId: Long,
        @RequestBody request: UpdateObjectiveRequest,
    ): ResponseEntity<ApiResponse<UpdateObjectiveResponse>> {
        return ApiResponse.ok(service.update(objectiveId, request.objective))
    }

    @PostMapping(ApiPath.Objective.SUGGEST)
    fun suggest(@RequestBody request: SuggestByObjectiveRequest): ResponseEntity<ApiResponse<SuggestByObjectiveResponse>> {
        val llmRequest = ObjectiveSuggestionRequest(request.objective)
        val llmResponse = mandalartLlmAgent.suggestObjective(llmRequest)
        
        val response = SuggestByObjectiveResponse(
            actions = llmResponse.json.actions
        )
        
        return ApiResponse.ok(response)
    }

}
