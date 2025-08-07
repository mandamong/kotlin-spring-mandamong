package com.mandamong.server.infrastructure.rabbitmq.consumer

import com.mandamong.server.infrastructure.email.entity.EmailOutbox
import com.mandamong.server.infrastructure.email.service.EmailService
import com.mandamong.server.infrastructure.rabbitmq.config.RabbitConfig
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class EmailMessageConsumer(
    private val service: EmailService,
) {

    @RabbitListener(queues = [RabbitConfig.EMAIL_QUEUE])
    fun consume(outbox: EmailOutbox) {
        service.sendEmail(outbox.email, outbox.code)
    }

}
