package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.infrastructure.gemini.GeminiService
import com.mandamong.server.mandalart.dto.request.SuggestRequest
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SubjectController(
    private val geminiService: GeminiService,
) {

    @PostMapping(ApiPath.Subject.SUGGEST)
    fun suggest(@RequestBody request: SuggestRequest): String {
        return geminiService.generateBySubject(request.prompt)
    }

    @PostMapping(ApiPath.Subject.CREATE)
    fun create() {

    }

    @PatchMapping(ApiPath.Subject.UPDATE)
    fun update() {

    }

}
