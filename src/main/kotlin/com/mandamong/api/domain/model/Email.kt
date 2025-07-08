package com.mandamong.api.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Email(
    @Column(name = "email")
    var value: String,
) {

    fun getLocalPart(): String {
        val index: Int = value.indexOf('@')
        return value.substring(0, index)
    }

    fun getDomain(): String {
        val index: Int = value.indexOf('@')
        return value.substring(index + 1)
    }

    companion object {
        fun from(value: String): Email {
            return Email(
                value = value,
            )
        }
    }
}
