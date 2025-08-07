package com.mandamong.server.infrastructure.email.entity

import com.mandamong.server.infrastructure.email.enums.EmailOutboxStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "email_outbox")
class EmailOutbox(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "code", nullable = false)
    val code: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: EmailOutboxStatus = EmailOutboxStatus.PENDING,
) {

    fun markSent() {
        this.status = EmailOutboxStatus.SENT
    }

    fun markFailed() {
        this.status = EmailOutboxStatus.FAILED
    }

}
