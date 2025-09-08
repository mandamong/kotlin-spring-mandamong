package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.dto.ApiResponse
import com.mandamong.server.common.dto.PaginationParameter
import com.mandamong.server.common.dto.PaginationResponse
import com.mandamong.server.mandalart.dto.CreateMandalartRequest
import com.mandamong.server.mandalart.dto.ReadMandalartResponse
import com.mandamong.server.mandalart.dto.ReadMandalartsResponse
import com.mandamong.server.mandalart.dto.UpdateMandalartRequest
import com.mandamong.server.mandalart.dto.UpdateMandalartResponse
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
    ): ResponseEntity<ApiResponse<ReadMandalartResponse>> = ApiResponse.ok(facade.create(request, loginUser.userId))

    @PatchMapping(ApiPath.Mandalart.UPDATE)
    fun update(
        @PathVariable mandalartId: Long,
        @RequestBody request: UpdateMandalartRequest,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<UpdateMandalartResponse>> =
        ApiResponse.ok(facade.update(mandalartId, request.mandalartName, loginUser.userId))

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
    ): ResponseEntity<ApiResponse<PaginationResponse<ReadMandalartsResponse>>> =
        ApiResponse.ok(facade.getMandalartsByUserId(paginationParameter, loginUser.userId))

    /**
         * 지정된 만다라트를 조회하여 반환합니다.
         *
         * 주어진 만다라트 ID와 인증된 사용자 정보를 사용해 해당 사용자가 접근 가능한 만다라트를 조회합니다.
         *
         * @param mandalartId 조회할 만다라트의 ID(경로 변수).
         * @param loginUser 인증된 사용자 정보 — 요청자의 userId를 소유자 검증에 사용합니다.
         * @return 조회된 만다라트 정보를 담은 ApiResponse<ReadMandalartResponse>를 포함한 ResponseEntity.
         */
        @GetMapping(ApiPath.Mandalart.MANDALART)
    fun readMandalart(
        @PathVariable mandalartId: Long,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<ReadMandalartResponse>> =
        ApiResponse.ok(facade.getMandalartById(mandalartId, loginUser.userId))
}
