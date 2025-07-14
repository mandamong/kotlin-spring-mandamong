package com.mandamong.server.mandalart.repository

import com.mandamong.server.mandalart.entity.Mandalart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MandalartRepository : JpaRepository<Mandalart, Long> {

    fun findByUserId(userId: Long): List<Mandalart>

}
