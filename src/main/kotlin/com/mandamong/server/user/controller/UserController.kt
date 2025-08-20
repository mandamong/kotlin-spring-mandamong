package com.mandamong.server.user.controller

import com.mandamong.server.auth.dto.LoginResponse
import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.dto.ApiResponse
import com.mandamong.server.user.dto.LoginUser
import com.mandamong.server.user.dto.ValidatePasswordRequest
import com.mandamong.server.user.dto.CreateUserRequest
import com.mandamong.server.user.dto.InitializePasswordRequest
import com.mandamong.server.user.dto.UpdateUserRequest
import com.mandamong.server.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val service: UserService,
) {

    @PostMapping(ApiPath.User.CREATE)
    fun create(
        @ModelAttribute request: CreateUserRequest,
    ): ResponseEntity<ApiResponse<LoginResponse>> {
        return ApiResponse.created(service.create(request))
    }

    @PatchMapping(ApiPath.User.UPDATE)
    fun update(
        @ModelAttribute request: UpdateUserRequest,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<String?>> {
        return ApiResponse.ok(service.update(request, loginUser.userId))
    }

    @DeleteMapping(ApiPath.User.DELETE)
    fun delete(@AuthenticationPrincipal loginUser: LoginUser): ResponseEntity<ApiResponse<Nothing>> {
        service.delete(loginUser.userId)
        return ApiResponse.deleted()
    }

    @PostMapping(ApiPath.User.VALIDATE_PASSWORD)
    fun validatePassword(
        @RequestBody request: ValidatePasswordRequest,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<Nothing>> {
        service.validatePassword(request.password, loginUser.userId)
        return ApiResponse.ok()
    }

    @PatchMapping(ApiPath.User.INITIALIZE_PASSWORD)
    fun initializePassword(@RequestBody request: InitializePasswordRequest): ResponseEntity<ApiResponse<UpdateUserRequest>> {
        return ApiResponse.ok(service.initializePassword(request.email))
    }

}
