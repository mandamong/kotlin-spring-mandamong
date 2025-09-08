package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.dto.ApiResponse
import com.mandamong.server.infrastructure.flowise.service.FlowiseService
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
    private val flowiseService: FlowiseService,
) {
    /**
         * 주제(subject)를 부분 업데이트하고 업데이트된 결과를 반환합니다.
         *
         * 주어진 subjectId에 대해 request.subject 내용으로 업데이트를 수행하며, 호출자의 userId(loginUser.userId)를 사용합니다.
         *
         * @param subjectId 업데이트할 주제의 식별자
         * @param request 업데이트할 필드들을 담은 요청 객체 (`request.subject`가 사용됨)
         * @param loginUser 인증된 사용자 정보 (업데이트 수행에 사용되는 `userId`)
         * @return 업데이트된 주제 정보를 담은 ApiResponse를 포함한 HTTP 응답
         */
        @PatchMapping(ApiPath.Subject.UPDATE)
    fun update(
        @PathVariable subjectId: Long,
        @RequestBody request: UpdateSubjectRequest,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<UpdateSubjectResponse>> =
        ApiResponse.ok(service.update(subjectId, request.subject, loginUser.userId))

    /**
     * 주제 기반 제안을 생성하여 반환한다.
     *
     * 주어진 요청을 바탕으로 제안 결과를 생성하고 ApiResponse로 래핑한 HTTP 응답을 반환한다. 이 엔드포인트는 인증이 필요하지 않다.
     *
     * @param request 제안 생성을 위한 입력 데이터
     * @return 제안 결과를 담은 ApiResponse를 포함한 ResponseEntity (성공 시 200 OK)
     */
    @PostMapping(ApiPath.Subject.SUGGEST)
    fun suggest(
        @RequestBody request: SuggestBySubjectRequest,
    ): ResponseEntity<ApiResponse<SuggestBySubjectResponse>> = ApiResponse.ok(flowiseService.suggestBySubject(request))
}
