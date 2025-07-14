package com.mandamong.server.mandalart.repository

import com.mandamong.server.mandalart.entity.Objective
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ObjectiveRepository : JpaRepository<Objective, Long> {

    fun findBySubjectId(subjectId: Long): List<Objective>
    fun deleteBySubjectId(subjectId: Long)

}
