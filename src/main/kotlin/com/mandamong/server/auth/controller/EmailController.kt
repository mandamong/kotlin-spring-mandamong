package com.mandamong.server.auth.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.response.ApiResponse
import com.mandamong.server.infrastructure.email.EmailService
import com.mandamong.server.user.dto.EmailVerificationRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EmailController(
    private val service: EmailService,
) {

    @PostMapping(ApiPath.Email.SEND)
    fun sendVerificationCode(@RequestBody request: EmailVerificationRequest): ResponseEntity<ApiResponse<Nothing>> {
        service.sendCode(request)
        return ApiResponse.ok()
    }

    @GetMapping(ApiPath.Email.VERIFY)
    fun verifyCode(@RequestParam email: String, @RequestParam code: String): ResponseEntity<ApiResponse<Nothing>> {
        service.verifyCode(email, code)
        return ApiResponse.ok()
    }

}
