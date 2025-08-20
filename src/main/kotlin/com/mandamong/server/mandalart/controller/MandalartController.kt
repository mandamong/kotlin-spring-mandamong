package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.dto.ApiResponse
import com.mandamong.server.common.dto.PaginationParameter
import com.mandamong.server.common.dto.PaginationResponse
import com.mandamong.server.mandalart.dto.CreateMandalartRequest
import com.mandamong.server.mandalart.dto.ReadMandalartResponse
import com.mandamong.server.mandalart.dto.UpdateMandalartRequest
import com.mandamong.server.mandalart.dto.UpdateMandalartResponse
import com.mandamong.server.mandalart.dto.ReadMandalartsResponse
import com.mandamong.server.mandalart.facade.MandalartFacade
import com.mandamong.server.user.dto.LoginUser
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
        @RequestBody request: CreateMandalartRequest,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<ReadMandalartResponse>> {
        return ApiResponse.ok(facade.create(request, loginUser.userId))
    }

    @PatchMapping(ApiPath.Mandalart.UPDATE)
    fun update(
        @PathVariable mandalartId: Long,
        @RequestBody request: UpdateMandalartRequest,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<UpdateMandalartResponse>> {
        return ApiResponse.ok(facade.update(mandalartId, request.newMandalartName, loginUser.userId))
    }

    @DeleteMapping(ApiPath.Mandalart.DELETE)
    fun delete(
        @PathVariable mandalartId: Long,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<Nothing>> {
        facade.delete(mandalartId, loginUser.userId)
        return ApiResponse.deleted()
    }

    @GetMapping(ApiPath.Mandalart.MANDALARTS)
    fun readMandalarts(
        paginationParameter: PaginationParameter,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<PaginationResponse<ReadMandalartsResponse>>> {
        return ApiResponse.ok(facade.getMandalartsByUserId(paginationParameter, loginUser.userId))
    }

    @GetMapping(ApiPath.Mandalart.MANDALART)
    fun readMandalart(
        @PathVariable mandalartId: Long,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<ReadMandalartResponse>> {
        return ApiResponse.ok(facade.getMandalartById(mandalartId, loginUser.userId))
    }

}
