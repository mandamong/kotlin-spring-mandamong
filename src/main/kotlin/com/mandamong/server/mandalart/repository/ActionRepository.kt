package com.mandamong.server.mandalart.repository

import com.mandamong.server.mandalart.entity.Action
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ActionRepository : JpaRepository<Action, Long> {

    @Query(
        """
        SELECT a FROM Action a
        JOIN FETCH a.objective o
        JOIN FETCH o.subject s
        JOIN FETCH s.mandalart
        WHERE a.id = :id
    """
    )
    fun findByIdWithAllData(id: Long): Action?

}
