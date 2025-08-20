package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.enums.Status

data class ReadMandalartsResponse(
    val name: String,
    val subject: String,
    val status: Status,
) {

    companion object {
        fun of(name: String, subject: String, status: Status): ReadMandalartsResponse {
            return ReadMandalartsResponse(
                name = name,
                subject = subject,
                status = status
            )
        }
    }

}
