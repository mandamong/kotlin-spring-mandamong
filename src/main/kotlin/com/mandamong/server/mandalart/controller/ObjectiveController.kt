package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.infrastructure.gemini.GeminiService
import com.mandamong.server.mandalart.dto.request.SuggestRequest
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ObjectiveController(
    private val geminiService: GeminiService,
) {

    @PostMapping(ApiPath.Objective.SUGGEST)
    fun suggest(@RequestBody request: SuggestRequest): String {
        return geminiService.generateByObjective(request.prompt)
    }

    @PostMapping(ApiPath.Objective.CREATE)
    fun create() {

    }

    @PatchMapping(ApiPath.Objective.UPDATE)
    fun update() {

    }

}
