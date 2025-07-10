package com.mandamong.api.auth.api

import com.mandamong.api.auth.api.dto.DuplicationResponse
import com.mandamong.api.auth.dao.MemberRepository
import com.mandamong.api.model.Email
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DuplicationApi(
    private val memberRepository: MemberRepository,
) {

    @GetMapping(ApiPath.Duplication.CHECK_NICKNAME)
    fun checkNickname(@RequestParam nickname: String): ResponseEntity<DuplicationResponse> {
        return ResponseEntity.ok()
            .body(DuplicationResponse(memberRepository.existsByNickname(nickname)))
    }

    @GetMapping(ApiPath.Duplication.CHECK_EMAIL)
    fun checkEmail(@RequestParam email: String): ResponseEntity<DuplicationResponse> {
        return ResponseEntity.ok()
            .body(DuplicationResponse(memberRepository.existsByEmail(Email.from(email))))
    }
}
