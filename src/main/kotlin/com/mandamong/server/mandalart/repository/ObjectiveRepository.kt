package com.mandamong.server.mandalart.repository

import com.mandamong.server.mandalart.entity.Objective
import com.mandamong.server.mandalart.enums.Status
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ObjectiveRepository : JpaRepository<Objective, Long> {

    fun countBySubjectIdAndStatus(subjectId: Long, status: Status): Int

}
