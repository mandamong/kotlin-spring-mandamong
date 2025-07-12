package com.mandamong.server.user.controller

import com.mandamong.server.auth.dto.response.EmailAuthResponse
import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.user.dto.request.EmailRegisterRequest
import com.mandamong.server.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val service: UserService,
) {

    @PostMapping(ApiPath.Auth.BASIC_REGISTER)
    fun basicRegister(@ModelAttribute emailRegisterRequest: EmailRegisterRequest): ResponseEntity<EmailAuthResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.basicRegister(emailRegisterRequest))
    }

}
