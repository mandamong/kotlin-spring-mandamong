package com.mandamong.server.mandalart.repository

import com.mandamong.server.mandalart.entity.Subject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubjectRepository : JpaRepository<Subject, Long> {

    fun findByMandalartId(mandalartId: Long): Subject?

}
