package com.mandamong.server.auth.controller

import com.mandamong.server.auth.dto.EmailLoginRequest
import com.mandamong.server.auth.dto.EmailLoginResponse
import com.mandamong.server.auth.service.AuthService
import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.response.ApiResponse
import com.mandamong.server.user.dto.AuthenticatedUser
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
    fun basicLogin(@RequestBody emailLoginRequest: EmailLoginRequest): ResponseEntity<ApiResponse<EmailLoginResponse>> {
        return ApiResponse.ok(service.basicLogin(emailLoginRequest.email, emailLoginRequest.password))
    }

    @PostMapping(ApiPath.Auth.LOGOUT)
    fun logout(@AuthenticationPrincipal loginUser: AuthenticatedUser): ResponseEntity<ApiResponse<Nothing>> {
        service.logout(loginUser.userId)
        return ApiResponse.deleted()
    }

}
