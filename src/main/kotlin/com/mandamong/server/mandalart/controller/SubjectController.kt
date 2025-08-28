package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.dto.ApiResponse
import com.mandamong.server.infrastructure.llm.MandalartLlmAgent
import com.mandamong.server.infrastructure.llm.dto.SubjectSuggestionRequest
import com.mandamong.server.mandalart.dto.SuggestBySubjectRequest
import com.mandamong.server.mandalart.dto.SuggestBySubjectResponse
import com.mandamong.server.mandalart.dto.UpdateSubjectRequest
import com.mandamong.server.mandalart.dto.UpdateSubjectResponse
import com.mandamong.server.mandalart.service.SubjectService
import com.mandamong.server.user.dto.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SubjectController(
    private val service: SubjectService,
    private val mandalartLlmAgent: MandalartLlmAgent,
) {

    @PatchMapping(ApiPath.Subject.UPDATE)
    fun update(
        @PathVariable subjectId: Long,
        @RequestBody request: UpdateSubjectRequest,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<UpdateSubjectResponse>> {
        return ApiResponse.ok(service.update(subjectId, request.subject, loginUser.userId))
    }

    @PostMapping(ApiPath.Subject.SUGGEST)
    fun suggest(@RequestBody request: SuggestBySubjectRequest): ResponseEntity<ApiResponse<SuggestBySubjectResponse>> {
        val llmRequest = SubjectSuggestionRequest(request.subject)
        val llmResponse = mandalartLlmAgent.suggestSubject(llmRequest)
        
        val response = SuggestBySubjectResponse(
            objectives = llmResponse.json.objectives,
            actions = llmResponse.json.actions
        )
        
        return ApiResponse.ok(response)
    }

}
