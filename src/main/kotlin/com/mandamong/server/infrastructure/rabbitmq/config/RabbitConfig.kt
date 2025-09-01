package com.mandamong.server.infrastructure.rabbitmq.config

import org.springframework.amqp.core.Queue
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {
    @Bean
    fun emailQueue(): Queue = Queue(EMAIL_QUEUE, true)

    @Bean
    fun messageConverter(): MessageConverter = Jackson2JsonMessageConverter()

    companion object {
        const val EMAIL_QUEUE = "email.queue"
    }
}
