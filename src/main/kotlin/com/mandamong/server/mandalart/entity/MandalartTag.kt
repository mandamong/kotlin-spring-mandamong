package com.mandamong.server.mandalart.entity

import com.mandamong.server.common.entity.BaseTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(
    name = "mandalart_tags",
    indexes = [
        Index(name = "mandalart_tags_mandalart_id_index", columnList = "mandalart_id"),
        Index(name = "mandalart_tags_tag_id_index", columnList = "tag_id"),
        Index(name = "mandalart_tags_unique_index", columnList = "mandalart_id, tag_id", unique = true)
    ]
)
class MandalartTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @JoinColumn(name = "mandalart_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val mandalart: Mandalart,
    @JoinColumn(name = "tag_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val tag: Tag,
) : BaseTimeEntity() {
    
    companion object {
        fun of(
            mandalart: Mandalart,
            tag: Tag,
        ): MandalartTag = MandalartTag(
            mandalart = mandalart,
            tag = tag,
        )
    }
    
}
