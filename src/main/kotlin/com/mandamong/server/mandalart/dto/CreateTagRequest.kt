package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.entity.Tag
import com.mandamong.server.user.entity.User

data class CreateTagRequest(
    val name: String,
) {
    fun toEntity(user: User): Tag = Tag.of(
        name = name,
        user = user,
    )
}
