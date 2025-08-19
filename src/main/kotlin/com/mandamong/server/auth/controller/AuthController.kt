package com.mandamong.server.auth.controller

import com.mandamong.server.auth.dto.LoginRequest
import com.mandamong.server.auth.dto.LoginResponse
import com.mandamong.server.auth.service.AuthService
import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.dto.ApiResponse
import com.mandamong.server.user.dto.LoginUser
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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
    fun login(
        @RequestBody request: LoginRequest,
        response: HttpServletResponse,
    ): ResponseEntity<ApiResponse<LoginResponse>> {
        return ApiResponse.ok(service.login(request.email, request.password, response))
    }

    @PostMapping(ApiPath.Auth.LOGOUT)
    fun logout(
        @AuthenticationPrincipal loginUser: LoginUser,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<ApiResponse<Nothing>> {
        service.logout(loginUser.userId, request, response)
        return ApiResponse.deleted()
    }

}
