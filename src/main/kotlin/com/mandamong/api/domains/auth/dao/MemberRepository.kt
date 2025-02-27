package com.mandamong.api.domains.auth.dao

import com.mandamong.api.domains.auth.domain.Member
import com.mandamong.api.domains.model.Email
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: Email): Member?
    fun existsByEmail(email: Email): Boolean
    fun existsByNickname(nickname: String): Boolean
    fun findByRefreshToken(refreshToken: String): Member?
}
