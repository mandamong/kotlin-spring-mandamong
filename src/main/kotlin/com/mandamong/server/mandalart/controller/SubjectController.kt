package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.infrastructure.gemini.GeminiService
import com.mandamong.server.mandalart.dto.request.MandalartUpdateRequest
import com.mandamong.server.mandalart.dto.request.SuggestRequest
import com.mandamong.server.mandalart.service.SubjectService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SubjectController(
    private val service: SubjectService,
    private val geminiService: GeminiService,
) {

    @PatchMapping(ApiPath.Subject.UPDATE)
    fun updateSubject(
        @PathVariable subjectId: Long,
        @RequestBody request: MandalartUpdateRequest,
    ): ResponseEntity<MandalartUpdateRequest> {
        return ResponseEntity.ok(service.updateSubject(subjectId, request))
    }

    @PostMapping(ApiPath.Subject.SUGGEST)
    fun suggest(@RequestBody request: SuggestRequest): String {
        return geminiService.generateBySubject(request.prompt)
    }

}
