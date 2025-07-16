package com.mandamong.server.auth.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.user.dto.DuplicationResponse
import com.mandamong.server.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DuplicationController(
    private val userService: UserService,
) {

    @GetMapping(ApiPath.Duplication.CHECK_NICKNAME)
    fun checkNickname(@RequestParam nickname: String): ResponseEntity<DuplicationResponse> {
        return ResponseEntity.ok(DuplicationResponse(userService.existsByNickname(nickname)))
    }

    @GetMapping(ApiPath.Duplication.CHECK_EMAIL)
    fun checkEmail(@RequestParam email: String): ResponseEntity<DuplicationResponse> {
        return ResponseEntity.ok(DuplicationResponse(userService.existsByEmail(email)))
    }

}
