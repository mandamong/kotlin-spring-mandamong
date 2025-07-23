package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.enums.Status

data class BasicData(
    val id: Long,
    val name: String,
    val status: Status,
) {

    companion object {
        fun of(id: Long, name: String, status: Status): BasicData = BasicData(id, name, status)
    }
    
}
