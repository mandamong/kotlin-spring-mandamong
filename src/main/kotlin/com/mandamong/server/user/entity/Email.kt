package com.mandamong.server.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Email(
    @Column(name = "email", nullable = false)
    val value: String,
) {

    companion object {
        fun from(value: String): Email = Email(value)
    }

}
