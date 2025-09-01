package com.mandamong.server.infrastructure.email.scheduler

import com.mandamong.server.common.annotation.lock.DistributedLock
import com.mandamong.server.common.error.exception.base.BusinessBaseException
import com.mandamong.server.infrastructure.email.entity.EmailOutbox
import com.mandamong.server.infrastructure.email.enums.EmailOutboxStatus
import com.mandamong.server.infrastructure.email.repository.EmailOutboxRepository
import com.mandamong.server.infrastructure.rabbitmq.producer.EmailMessageProducer
import com.mandamong.server.user.repository.EmailVerificationRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Component
class EmailOutboxScheduler(
    private val repository: EmailOutboxRepository,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val producer: EmailMessageProducer,
) {
    @DistributedLock(name = "EMAIL", key = "'OUTBOX'", maxWaitForLock = 15, autoUnlockAfter = 30)
    @Transactional
    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    fun publish() {
        val outboxes = repository.findTop10ByStatus(EmailOutboxStatus.PENDING)
        outboxes.forEach(::processOutbox)
    }

    private fun processOutbox(outbox: EmailOutbox) {
        try {
            producer.produce(outbox)
            outbox.markSent()
            emailVerificationRepository.set(outbox.email, outbox.code)
        } catch (e: Exception) {
            outbox.markFailed()
            throw BusinessBaseException()
        }
    }
}
