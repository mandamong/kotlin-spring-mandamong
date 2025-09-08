package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.dto.ApiResponse
import com.mandamong.server.infrastructure.flowise.service.FlowiseService
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
    private val flowiseService: FlowiseService,
) {
    /**
         * 주어진 목표(objective)를 갱신하고 갱신된 결과를 응답으로 반환합니다.
         *
         * @param objectiveId 갱신할 목표의 식별자(ID).
         * @param request 갱신할 목표 데이터를 담은 요청 객체(요청의 `objective` 필드가 사용됩니다).
         * @return 갱신된 목표 정보를 담은 ApiResponse 래핑 객체를 포함한 HTTP 응답(Entity).
         */
        @PatchMapping(ApiPath.Objective.UPDATE)
    fun update(
        @PathVariable objectiveId: Long,
        @RequestBody request: UpdateObjectiveRequest,
    ): ResponseEntity<ApiResponse<UpdateObjectiveResponse>> =
        ApiResponse.ok(service.update(objectiveId, request.objective))

    /**
         * 주어진 목표 정보를 바탕으로 Flowise에 제안 요청을 보내고, 결과를 ApiResponse로 감싼 HTTP 응답을 반환한다.
         *
         * @param request 제안 생성에 사용되는 목표 기반 입력 데이터
         * @return 요청 처리에 성공한 제안 결과를 담은 ApiResponse를 포함한 ResponseEntity (HTTP 200)
         */
        @PostMapping(ApiPath.Objective.SUGGEST)
    fun suggest(
        @RequestBody request: SuggestByObjectiveRequest,
    ): ResponseEntity<ApiResponse<SuggestByObjectiveResponse>> =
        ApiResponse.ok(flowiseService.suggestByObjective(request))
}
