package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.entity.Tag
import java.time.LocalDateTime

data class TagResponse(
    val id: Long,
    val name: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(tag: Tag): TagResponse = TagResponse(
            id = tag.id,
            name = tag.name,
            createdAt = tag.createdAt,
            updatedAt = tag.updatedAt,
        )
    }
}
