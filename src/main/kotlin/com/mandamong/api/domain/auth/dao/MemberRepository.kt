package com.mandamong.api.domain.auth.dao

import com.mandamong.api.domain.auth.domain.Member
import com.mandamong.api.domain.model.Email
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {

    fun findByEmail(email: Email): Member?
    fun existsByEmail(email: Email): Boolean
    fun existsByNickname(nickname: String): Boolean
    fun findByRefreshToken(refreshToken: String): Member?
}
