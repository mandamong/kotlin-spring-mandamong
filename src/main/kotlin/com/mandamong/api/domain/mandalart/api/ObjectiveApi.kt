package com.mandamong.api.domain.mandalart.api

import com.mandamong.api.domain.mandalart.api.dto.ObjectiveSuggestRequest
import com.mandamong.api.global.common.ApiPath
import com.mandamong.api.infrastructure.application.GeminiService
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ObjectiveApi(
    private val geminiService: GeminiService,
) {

    @PostMapping(ApiPath.Objective.SUGGEST)
    fun suggest(@RequestBody request: ObjectiveSuggestRequest): String {
        return geminiService.generateByObjective(request.objective)
    }

    @PostMapping(ApiPath.Objective.CREATE)
    fun create() {

    }

    @PatchMapping(ApiPath.Objective.UPDATE)
    fun update() {

    }
}
