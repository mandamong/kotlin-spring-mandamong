package com.mandamong.server.user.controller

import com.mandamong.server.auth.dto.EmailLoginResponse
import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.response.ApiResponse
import com.mandamong.server.user.dto.EmailRegisterRequest
import com.mandamong.server.user.dto.LoginUser
import com.mandamong.server.user.dto.PasswordValidationRequest
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
    fun basicRegister(
        @ModelAttribute emailRegisterRequest: EmailRegisterRequest,
    ): ResponseEntity<ApiResponse<EmailLoginResponse>> {
        return ApiResponse.created(service.basicRegister(emailRegisterRequest))
    }

    @PatchMapping(ApiPath.User.UPDATE_NICKNAME)
    fun updateNickname(
        @RequestBody request: UserUpdateRequest,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<UserUpdateRequest>> {
        return ApiResponse.ok(service.updateNickname(request, loginUser))
    }

    @PostMapping(ApiPath.User.VALIDATE_PASSWORD)
    fun validatePassword(
        @RequestBody request: PasswordValidationRequest,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<Nothing>> {
        service.validatePassword(request, loginUser)
        return ApiResponse.ok()
    }

    @DeleteMapping(ApiPath.User.DELETE)
    fun delete(@AuthenticationPrincipal loginUser: LoginUser): ResponseEntity<ApiResponse<Nothing>> {
        service.deleteById(loginUser.userId)
        return ApiResponse.deleted()
    }

}
