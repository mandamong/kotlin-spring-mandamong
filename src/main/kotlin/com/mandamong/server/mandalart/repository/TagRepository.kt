package com.mandamong.server.mandalart.repository

import com.mandamong.server.mandalart.entity.Tag
import com.mandamong.server.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, Long> {
    
    fun findByUserOrderByCreatedAtDesc(user: User): List<Tag>
    
    @Query("""
        SELECT t FROM Tag t 
        WHERE t.user = :user 
        AND t.name LIKE %:keyword% 
        ORDER BY t.createdAt DESC
    """)
    fun findByUserAndNameContaining(@Param("user") user: User, @Param("keyword") keyword: String): List<Tag>
}
