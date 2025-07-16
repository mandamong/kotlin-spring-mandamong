package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.mandalart.dto.MandalartCreateRequest
import com.mandamong.server.mandalart.dto.MandalartUpdateRequest
import com.mandamong.server.mandalart.dto.MandalartDataResponse
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
        @AuthenticationPrincipal user: AuthenticatedUser,
    ): ResponseEntity<MandalartDataResponse> {
        return ResponseEntity.ok(facade.create(request, user))
    }

    @GetMapping(ApiPath.Mandalart.MANDALARTS)
    fun getMandalarts(@AuthenticationPrincipal user: AuthenticatedUser): ResponseEntity<List<MandalartDataResponse>> {
        return ResponseEntity.ok(facade.getMandalarts(user.userId))
    }

    @GetMapping(ApiPath.Mandalart.MANDALART)
    fun getMandalart(@PathVariable mandalartId: Long): ResponseEntity<MandalartDataResponse> {
        return ResponseEntity.ok(facade.getMandalart(mandalartId))
    }

    @PatchMapping(ApiPath.Mandalart.UPDATE_NAME)
    fun updateName(
        @PathVariable mandalartId: Long,
        @RequestBody request: MandalartUpdateRequest,
    ): ResponseEntity<MandalartUpdateRequest> {
        return ResponseEntity.ok(facade.updateName(mandalartId, request))
    }

    @DeleteMapping(ApiPath.Mandalart.DELETE)
    fun delete(@PathVariable mandalartId: Long): ResponseEntity<Nothing> {
        facade.delete(mandalartId)
        return ResponseEntity.noContent().build()
    }

}
