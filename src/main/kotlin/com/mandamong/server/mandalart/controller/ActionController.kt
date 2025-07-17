package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.response.ApiResponse
import com.mandamong.server.mandalart.dto.MandalartUpdateRequest
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

    @PatchMapping(ApiPath.Action.UPDATE)
    fun update(
        @PathVariable actionId: Long,
        @RequestBody request: MandalartUpdateRequest,
    ): ResponseEntity<ApiResponse<MandalartUpdateRequest>> {
        return ApiResponse.ok(service.update(actionId, request))
    }

}
