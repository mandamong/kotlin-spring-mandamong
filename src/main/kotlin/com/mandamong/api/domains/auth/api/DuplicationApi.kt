package com.mandamong.api.domains.auth.api

import com.mandamong.api.domains.auth.api.dto.DuplicationResponse
import com.mandamong.api.domains.auth.dao.MemberRepository
import com.mandamong.api.domains.model.Email
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/members/duplication")
class DuplicationApi(
    private val memberRepository: MemberRepository
) {
    @GetMapping("/nickname")
    fun checkNickname(@RequestParam nickname: String): ResponseEntity<DuplicationResponse> {
        return ResponseEntity.ok()
            .body(DuplicationResponse(memberRepository.existsByNickname(nickname)))
    }

    @GetMapping("/email")
    fun checkEmail(@RequestParam email: String): ResponseEntity<DuplicationResponse> {
        return ResponseEntity.ok()
            .body(DuplicationResponse(memberRepository.existsByEmail(Email.from(email))))
    }
}
