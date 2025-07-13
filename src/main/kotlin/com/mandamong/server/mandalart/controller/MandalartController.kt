package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.mandalart.dto.request.MandalartCreateRequest
import com.mandamong.server.mandalart.dto.response.MandalartDataResponse
import com.mandamong.server.mandalart.facade.MandalartFacade
import com.mandamong.server.user.dto.AuthenticatedUser
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
        return facade.create(request, user)
    }

    @GetMapping(ApiPath.Mandalart.MANDALARTS)
    fun mandalarts() {

    }

    @GetMapping(ApiPath.Mandalart.MANDALART)
    fun mandalart(@PathVariable mandalartId: Long) {

    }

    @PutMapping(ApiPath.Mandalart.UPDATE)
    fun update() {

    }

    @DeleteMapping(ApiPath.Mandalart.DELETE)
    fun delete(@PathVariable mandalartId: Long) {
        facade.delete(mandalartId)
    }

}
