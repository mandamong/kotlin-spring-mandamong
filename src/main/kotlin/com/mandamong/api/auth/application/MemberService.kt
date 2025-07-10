package com.mandamong.api.auth.application


import com.mandamong.api.auth.dao.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val repository: MemberRepository,
) {

    fun findByRefreshToken(token: String): Long {
        return repository.findByRefreshToken(token)!!.id
    }
}
