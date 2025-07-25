package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.response.ApiResponse
import com.mandamong.server.infrastructure.gemini.GeminiService
import com.mandamong.server.mandalart.dto.BasicData
import com.mandamong.server.mandalart.dto.MandalartUpdateRequest
import com.mandamong.server.mandalart.dto.SuggestByObjectiveResponse
import com.mandamong.server.mandalart.dto.SuggestRequest
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
    private val geminiService: GeminiService,
) {

    @PatchMapping(ApiPath.Objective.UPDATE)
    fun update(
        @PathVariable objectiveId: Long,
        @RequestBody request: MandalartUpdateRequest,
    ): ResponseEntity<ApiResponse<BasicData>> {
        return ApiResponse.ok(service.update(objectiveId, request))
    }

    @PostMapping(ApiPath.Objective.SUGGEST)
    fun suggest(@RequestBody request: SuggestRequest): ResponseEntity<ApiResponse<SuggestByObjectiveResponse>> {
        return ApiResponse.ok(geminiService.generateByObjective(request.prompt))
    }

}
