package com.mandamong.api.domains.auth.dao

import com.mandamong.api.domains.auth.domain.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member
    fun existsByEmail(email: String): Boolean
    fun existsByNickname(nickname: String): Boolean
}
