package com.mandamong.server.auth.controller

import com.mandamong.server.auth.dto.RefreshRequest
import com.mandamong.server.auth.dto.RefreshResponse
import com.mandamong.server.auth.service.RefreshService
import com.mandamong.server.common.constants.ApiPath
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RefreshController(
    private val service: RefreshService,
) {

    @PostMapping(ApiPath.Auth.REFRESH)
    fun refreshToken(@RequestBody request: RefreshRequest): ResponseEntity<RefreshResponse> {
        return ResponseEntity.ok(service.refresh(request))
    }

}
