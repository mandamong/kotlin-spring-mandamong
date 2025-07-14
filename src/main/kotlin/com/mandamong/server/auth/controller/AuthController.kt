package com.mandamong.server.auth.controller

import com.mandamong.server.auth.dto.request.EmailLoginRequest
import com.mandamong.server.auth.dto.response.EmailLoginResponse
import com.mandamong.server.auth.service.AuthService
import com.mandamong.server.common.constants.ApiPath
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val service: AuthService,
) {

    @PostMapping(ApiPath.Auth.LOGIN)
    fun basicLogin(@RequestBody emailLoginRequest: EmailLoginRequest): ResponseEntity<EmailLoginResponse> {
        return ResponseEntity.ok(service.basicLogin(emailLoginRequest.email, emailLoginRequest.password))
    }

}
