package com.mandamong.server.mandalart.dto

data class Pair(
    val id: Long,
    val name: String,
) {

    companion object {
        fun of(id: Long, name: String): Pair = Pair(id, name)
    }
    
}
