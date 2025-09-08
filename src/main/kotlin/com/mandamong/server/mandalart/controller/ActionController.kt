package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.dto.ApiResponse
import com.mandamong.server.mandalart.dto.UpdateActionRequest
import com.mandamong.server.mandalart.dto.UpdateActionResponse
import com.mandamong.server.mandalart.service.ActionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ActionController(
    private val service: ActionService,
) {
    /**
         * 지정한 액션의 내용을 업데이트하고 업데이트된 결과를 반환합니다.
         *
         * @param actionId 업데이트할 액션의 ID
         * @param request 업데이트할 필드들을 포함한 요청 객체 (`action`: 변경할 작업 내용, `status`: 변경할 상태)
         * @return 업데이트된 액션 정보를 담은 ApiResponse (HTTP 200)
         */
        @PatchMapping(ApiPath.Action.UPDATE)
    fun update(
        @PathVariable actionId: Long,
        @RequestBody request: UpdateActionRequest,
    ): ResponseEntity<ApiResponse<UpdateActionResponse>> =
        ApiResponse.ok(service.update(actionId, request.action, request.status))
}
