package com.mandamong.api.domain.auth.api

import com.mandamong.api.domain.auth.api.dto.DuplicationResponse
import com.mandamong.api.domain.auth.dao.MemberRepository
import com.mandamong.api.domain.model.Email
import com.mandamong.api.global.common.ApiPath
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
