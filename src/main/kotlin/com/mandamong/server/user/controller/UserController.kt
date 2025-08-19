package com.mandamong.server.user.controller

import com.mandamong.server.auth.dto.LoginResponse
import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.dto.ApiResponse
import com.mandamong.server.user.dto.LoginUser
import com.mandamong.server.user.dto.PasswordValidationRequest
import com.mandamong.server.user.dto.RegisterRequest
import com.mandamong.server.user.dto.UserPasswordInitializeRequest
import com.mandamong.server.user.dto.UserUpdateRequest
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
        @ModelAttribute request: RegisterRequest,
    ): ResponseEntity<ApiResponse<LoginResponse>> {
        return ApiResponse.created(service.create(request))
    }

    @PatchMapping(ApiPath.User.UPDATE_NICKNAME)
    fun updateNickname(
        @RequestBody request: UserUpdateRequest,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<UserUpdateRequest>> {
        return ApiResponse.ok(service.updateNickname(request.updated, loginUser.userId))
    }

    @PostMapping(ApiPath.User.VALIDATE_PASSWORD)
    fun validatePassword(
        @RequestBody request: PasswordValidationRequest,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<Nothing>> {
        service.validatePassword(request.password, loginUser.userId)
        return ApiResponse.ok()
    }

    @PatchMapping(ApiPath.User.UPDATE_PASSWORD)
    fun updatePassword(
        @RequestBody request: UserUpdateRequest,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<Nothing>> {
        service.updatePassword(request.updated, loginUser.userId)
        return ApiResponse.ok()
    }

    @PatchMapping(ApiPath.User.INITIALIZE_PASSWORD)
    fun initializePassword(@RequestBody request: UserPasswordInitializeRequest): ResponseEntity<ApiResponse<UserUpdateRequest>> {
        return ApiResponse.ok(service.initializePassword(request.email))
    }

    @DeleteMapping(ApiPath.User.DELETE)
    fun delete(@AuthenticationPrincipal loginUser: LoginUser): ResponseEntity<ApiResponse<Nothing>> {
        service.delete(loginUser.userId)
        return ApiResponse.deleted()
    }

}
