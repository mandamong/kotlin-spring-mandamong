package com.mandamong.server.auth.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.response.ApiResponse
import com.mandamong.server.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DuplicationController(
    private val userService: UserService,
) {

    @GetMapping(ApiPath.Duplication.EMAIL)
    fun validateEmailDuplication(@RequestParam email: String): ResponseEntity<ApiResponse<Nothing>> {
        userService.validateEmailDuplication(email)
        return ApiResponse.ok()
    }

    @GetMapping(ApiPath.Duplication.NICKNAME)
    fun validateNicknameDuplication(@RequestParam nickname: String): ResponseEntity<ApiResponse<Nothing>> {
        userService.validateNicknameDuplication(nickname)
        return ApiResponse.ok()
    }

}
