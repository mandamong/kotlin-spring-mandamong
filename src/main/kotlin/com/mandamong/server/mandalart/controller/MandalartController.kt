package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.response.ApiResponse
import com.mandamong.server.mandalart.dto.MandalartCreateRequest
import com.mandamong.server.mandalart.dto.MandalartDataResponse
import com.mandamong.server.mandalart.dto.MandalartUpdateRequest
import com.mandamong.server.mandalart.facade.MandalartFacade
import com.mandamong.server.user.dto.AuthenticatedUser
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MandalartController(
    private val facade: MandalartFacade,
) {

    @PostMapping(ApiPath.Mandalart.CREATE)
    fun create(
        @RequestBody request: MandalartCreateRequest,
        @AuthenticationPrincipal loginUser: AuthenticatedUser,
    ): ResponseEntity<ApiResponse<MandalartDataResponse>> {
        return ApiResponse.ok(facade.create(request, loginUser))
    }

    @PatchMapping(ApiPath.Mandalart.UPDATE_NAME)
    fun update(
        @PathVariable mandalartId: Long,
        @RequestBody request: MandalartUpdateRequest,
    ): ResponseEntity<ApiResponse<MandalartUpdateRequest>> {
        return ApiResponse.ok(facade.update(mandalartId, request))
    }

    @DeleteMapping(ApiPath.Mandalart.DELETE)
    fun delete(@PathVariable mandalartId: Long): ResponseEntity<ApiResponse<Nothing>> {
        facade.delete(mandalartId)
        return ApiResponse.deleted()
    }

    @GetMapping(ApiPath.Mandalart.MANDALARTS)
    fun getMandalarts(
        @AuthenticationPrincipal loginUser: AuthenticatedUser,
    ): ResponseEntity<ApiResponse<List<MandalartDataResponse>>> {
        return ApiResponse.ok(facade.getMandalartsByUserId(loginUser.userId))
    }

    @GetMapping(ApiPath.Mandalart.MANDALART)
    fun getMandalart(@PathVariable mandalartId: Long): ResponseEntity<ApiResponse<MandalartDataResponse>> {
        return ApiResponse.ok(facade.getByMandalartId(mandalartId))
    }

}
