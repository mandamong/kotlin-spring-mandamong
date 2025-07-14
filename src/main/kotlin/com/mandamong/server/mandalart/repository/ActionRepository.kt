package com.mandamong.server.mandalart.repository

import com.mandamong.server.mandalart.entity.Action
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ActionRepository : JpaRepository<Action, Long> {

    fun findByObjectiveId(objectiveId: Long): List<Action>
    fun deleteByObjectiveId(objectiveId: Long)

}
