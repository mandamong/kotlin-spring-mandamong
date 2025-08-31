package com.mandamong.server.infrastructure.discord.service

import com.mandamong.server.common.util.log.log
import com.mandamong.server.infrastructure.discord.dto.DiscordPayload
import com.mandamong.server.infrastructure.discord.model.DiscordEmbed
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class DiscordService(
    private val discordClient: WebClient,
) {

    private val log = log()

    @Async
    fun notifyException(e: Exception, requestInformation: String? = null) {
        val embed = DiscordEmbed.of(e, requestInformation)
        val payload = DiscordPayload(listOf(embed))
        sendNotification(payload)
        log.info("EXCEPTION_NOTIFICATION_SENT")
    }

    private fun sendNotification(payload: DiscordPayload) {
        discordClient.post()
            .bodyValue(payload)
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
    }

}

