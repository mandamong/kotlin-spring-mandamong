package com.mandamong.server.infrastructure.rabbitmq.producer

import com.mandamong.server.infrastructure.email.entity.EmailOutbox
import com.mandamong.server.infrastructure.rabbitmq.config.RabbitConfig
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class EmailMessageProducer(
    private val rabbitTemplate: RabbitTemplate,
) {

    fun produce(outbox: EmailOutbox) {
        rabbitTemplate.convertAndSend(RabbitConfig.EMAIL_QUEUE, outbox)
    }

}
