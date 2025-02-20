package com.mandamong.api.domains.auth.api

import com.mandamong.api.domains.auth.api.dto.EmailAuthResponse
import com.mandamong.api.domains.auth.api.dto.EmailLoginRequest
import com.mandamong.api.domains.auth.api.dto.EmailSignupRequest
import com.mandamong.api.domains.auth.application.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthApi(
    private val authService: AuthService
) {
    @PostMapping("/members")
    fun basicSignUp(@ModelAttribute emailSignupRequest: EmailSignupRequest): ResponseEntity<EmailAuthResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(authService.basicSignup(emailSignupRequest))
    }

    @PostMapping("/auth/login")
    fun basicLogin(@RequestBody emailLoginRequest: EmailLoginRequest): ResponseEntity<EmailAuthResponse> {
        return ResponseEntity.ok()
            .body(authService.basicLogin(emailLoginRequest))
    }
}
