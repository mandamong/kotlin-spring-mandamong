package com.mandamong.server.mandalart.repository

import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.entity.MandalartTag
import com.mandamong.server.mandalart.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MandalartTagRepository : JpaRepository<MandalartTag, Long> {
    
    fun existsByMandalartAndTag(mandalart: Mandalart, tag: Tag): Boolean
    
    @Modifying
    @Query("""
        DELETE FROM MandalartTag mt 
        WHERE mt.mandalart.id = :mandalartId AND mt.tag.id = :tagId
    """)
    fun deleteByMandalartIdAndTagId(@Param("mandalartId") mandalartId: Long, @Param("tagId") tagId: Long)
}
