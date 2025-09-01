package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.entity.Subject
import com.mandamong.server.mandalart.enums.Status

data class UpdateSubjectResponse(
    val id: Long,
    val subject: String,
    val status: Status,
) {
    companion object {
        fun of(subject: Subject): UpdateSubjectResponse =
            UpdateSubjectResponse(
                id = subject.id,
                subject = subject.subject,
                status = subject.status,
            )
    }
}
