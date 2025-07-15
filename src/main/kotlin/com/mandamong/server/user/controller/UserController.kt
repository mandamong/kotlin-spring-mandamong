package com.mandamong.server.user.controller

import com.mandamong.server.auth.dto.response.EmailLoginResponse
import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.user.dto.AuthenticatedUser
import com.mandamong.server.user.dto.request.EmailRegisterRequest
import com.mandamong.server.user.dto.request.UserUpdateRequest
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
    fun basicRegister(@ModelAttribute emailRegisterRequest: EmailRegisterRequest): ResponseEntity<EmailLoginResponse> {
        return ResponseEntity.ok(service.basicRegister(emailRegisterRequest))
    }

    @PatchMapping(ApiPath.User.UPDATE_NICKNAME)
    fun updateNickname(
        @RequestBody request: UserUpdateRequest,
        @AuthenticationPrincipal user: AuthenticatedUser,
    ): ResponseEntity<UserUpdateRequest> {
        return ResponseEntity.ok(service.updateNickname(request, user.userId))
    }

    @DeleteMapping(ApiPath.User.DELETE)
    fun delete(@AuthenticationPrincipal user: AuthenticatedUser): ResponseEntity<Nothing> {
        service.deleteById(user.userId)
        return ResponseEntity.noContent().build()
    }

}
