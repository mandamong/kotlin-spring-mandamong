package com.mandamong.server.auth.controller

import com.mandamong.server.auth.dto.TokenRefreshRequest
import com.mandamong.server.auth.dto.TokenRefreshResponse
import com.mandamong.server.auth.service.RefreshService
import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.dto.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RefreshController(
    private val service: RefreshService,
) {

    @PostMapping(ApiPath.Auth.REFRESH)
    fun refresh(@RequestBody request: TokenRefreshRequest): ResponseEntity<ApiResponse<TokenRefreshResponse>> {
        return ApiResponse.ok(service.refresh(request.refreshToken))
    }

}
