package com.mandamong.server.mandalart.repository

import com.mandamong.server.mandalart.entity.Mandalart
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MandalartRepository : JpaRepository<Mandalart, Long> {

    @EntityGraph(attributePaths = ["subject", "subject.objectives", "subject.objectives.actions"])
    @Query("SELECT m FROM Mandalart m WHERE m.user.id = :userId")
    fun findByUserIdWithFullData(userId: Long): List<Mandalart>?

    @EntityGraph(attributePaths = ["subject", "subject.objectives", "subject.objectives.actions"])
    @Query("SELECT m FROM Mandalart m WHERE m.id = :id")
    fun findByIdWithFullData(id: Long): Mandalart?

}
