package com.mandamong.api.auth.api

import com.mandamong.api.auth.api.dto.RefreshRequest
import com.mandamong.api.auth.api.dto.RefreshResponse
import com.mandamong.api.auth.application.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RefreshApi(
    private val authService: AuthService,
) {

    @PostMapping(ApiPath.Auth.REFRESH)
    fun refreshToken(@RequestBody refreshRequest: RefreshRequest): ResponseEntity<RefreshResponse> {
        val response: RefreshResponse = authService.refresh(refreshRequest)
        return ResponseEntity.ok(response)
    }
}
