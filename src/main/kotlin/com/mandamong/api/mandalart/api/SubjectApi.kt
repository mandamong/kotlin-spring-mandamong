package com.mandamong.api.mandalart.api

import com.mandamong.api.auth.api.ApiPath
import com.mandamong.api.infrastructure.application.GeminiService
import com.mandamong.api.mandalart.api.dto.SubjectSuggestRequest
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SubjectApi(
    private val geminiService: GeminiService,
) {

    @PostMapping(ApiPath.Subject.SUGGEST)
    fun suggest(@RequestBody request: SubjectSuggestRequest): String {
        return geminiService.generateBySubject(request.subject)
    }

    @PostMapping(ApiPath.Subject.CREATE)
    fun create() {

    }

    @PatchMapping(ApiPath.Subject.UPDATE)
    fun update() {

    }
}
