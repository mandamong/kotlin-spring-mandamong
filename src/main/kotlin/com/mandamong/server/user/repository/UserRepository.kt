package com.mandamong.server.user.repository

import com.mandamong.server.user.entity.Email
import com.mandamong.server.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: Email): User?
    fun existsByEmail(email: Email): Boolean
    fun existsByNickname(nickname: String): Boolean

}
