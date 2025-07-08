package com.mandamong.api.domain.auth.api

import com.mandamong.api.domain.auth.api.dto.EmailAuthResponse
import com.mandamong.api.domain.auth.api.dto.EmailLoginRequest
import com.mandamong.api.domain.auth.api.dto.EmailSignupRequest
import com.mandamong.api.domain.auth.application.AuthService
import com.mandamong.api.global.common.ApiPath
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthApi(
    private val authService: AuthService,
) {

    @PostMapping(ApiPath.Auth.BASIC_REGISTER)
    fun basicRegister(@ModelAttribute emailSignupRequest: EmailSignupRequest): ResponseEntity<EmailAuthResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(authService.basicSignup(emailSignupRequest))
    }

    @PostMapping(ApiPath.Auth.BASIC_LOGIN)
    fun basicLogin(@RequestBody emailLoginRequest: EmailLoginRequest): ResponseEntity<EmailAuthResponse> {
        return ResponseEntity.ok()
            .body(authService.basicLogin(emailLoginRequest))
    }
}
