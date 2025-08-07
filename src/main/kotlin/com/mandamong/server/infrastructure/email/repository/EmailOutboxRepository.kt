package com.mandamong.server.infrastructure.email.repository

import com.mandamong.server.infrastructure.email.entity.EmailOutbox
import com.mandamong.server.infrastructure.email.enums.EmailOutboxStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailOutboxRepository : JpaRepository<EmailOutbox, String> {

    fun findTop10ByStatus(status: EmailOutboxStatus): List<EmailOutbox>

}
