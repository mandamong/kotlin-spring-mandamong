package com.mandamong.api.domain.auth.api

import com.mandamong.api.domain.auth.api.dto.EmailVerificationRequest
import com.mandamong.api.domain.auth.api.dto.EmailVerificationResponse
import com.mandamong.api.infrastructure.application.EmailService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth/email/verification")
class EmailApi(
    private val emailService: EmailService,
) {
    @PostMapping
    fun sendCode(@RequestBody emailVerificationRequest: EmailVerificationRequest): ResponseEntity<Void> {
        emailService.sendCode(emailVerificationRequest.email)
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun verifyCode(
        @RequestParam("email") email: String,
        @RequestParam("code") code: String,
    ): ResponseEntity<EmailVerificationResponse> {
        val response: EmailVerificationResponse = emailService.verifyCode(email, code)
        return ResponseEntity.ok(response)
    }
}
