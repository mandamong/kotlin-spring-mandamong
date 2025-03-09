package com.mandamong.api.domain.auth.api

import com.mandamong.api.domain.auth.api.dto.RefreshRequest
import com.mandamong.api.domain.auth.api.dto.RefreshResponse
import com.mandamong.api.domain.auth.application.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth/token")
class RefreshApi(
    private val authService: AuthService,
) {
    @PostMapping("/refresh")
    fun refreshToken(@RequestBody refreshRequest: RefreshRequest): ResponseEntity<RefreshResponse> {
        val response: RefreshResponse = authService.refresh(refreshRequest)
        return ResponseEntity.ok(response)
    }
}
