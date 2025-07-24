package com.mandamong.server.auth.controller

import com.mandamong.server.auth.dto.LoginRequest
import com.mandamong.server.auth.dto.LoginResponse
import com.mandamong.server.auth.service.AuthService
import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.response.ApiResponse
import com.mandamong.server.user.dto.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val service: AuthService,
) {

    @PostMapping(ApiPath.Auth.LOGIN)
    fun login(@RequestBody request: LoginRequest): ResponseEntity<ApiResponse<LoginResponse>> {
        return ApiResponse.ok(service.login(request))
    }

    @PostMapping(ApiPath.Auth.LOGOUT)
    fun logout(@AuthenticationPrincipal loginUser: LoginUser): ResponseEntity<ApiResponse<Nothing>> {
        service.logout(loginUser)
        return ApiResponse.deleted()
    }

}
