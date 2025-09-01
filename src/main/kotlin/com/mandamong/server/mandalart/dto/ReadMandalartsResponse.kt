package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.enums.Status

open class ReadMandalartsResponse(
    val id: Long,
    val name: String,
    val subject: String,
    val status: Status,
) {
    companion object {
        fun of(mandalart: Mandalart): ReadMandalartsResponse =
            ReadMandalartsResponse(
                id = mandalart.id,
                name = mandalart.name,
                subject = mandalart.subject!!.subject,
                status = mandalart.status,
            )
    }
}
