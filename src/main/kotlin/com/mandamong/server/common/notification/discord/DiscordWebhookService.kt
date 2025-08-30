package com.mandamong.server.common.notification.discord

import com.mandamong.server.common.util.log.log
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class DiscordWebhookService(
    @Qualifier("discordWebhookClient") private val webClient: WebClient,
) {

    private val log = log()

    @Async
    fun sendErrorNotification(exception: Exception, requestInfo: String? = null) {
        try {
            val embed = createErrorEmbed(exception, requestInfo)
            val payload = DiscordWebhookPayload(embeds = listOf(embed))

            sendWebhook(payload)
            log.info("Discord error notification sent successfully")
        } catch (e: Exception) {
            log.error("Failed to send Discord notification", e)
        }
    }

    private fun createErrorEmbed(exception: Exception, requestInfo: String?): DiscordEmbed {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val fields = mutableListOf<DiscordField>().apply {
            add(DiscordField("Exception Type", exception.javaClass.simpleName, true))
            add(DiscordField("Message", exception.message ?: "No message", true))
            add(DiscordField("Timestamp", timestamp, true))

            requestInfo?.let {
                add(DiscordField("Request Info", it, false))
            }

            exception.stackTrace.take(5).let { stackTrace ->
                if (stackTrace.isNotEmpty()) {
                    val stackTraceString = stackTrace.joinToString("\n") {
                        "${it.className}.${it.methodName}:${it.lineNumber}"
                    }
                    add(DiscordField("Stack Trace (Top 5)", "```\n$stackTraceString\n```", false))
                }
            }
        }

        return DiscordEmbed(
            title = "🚨 Server Error (500) Detected",
            description = "A server error has occurred in the application",
            color = 16711680, // Red color
            fields = fields,
            footer = DiscordFooter("Mandamong Server Alert"),
            timestamp = "${LocalDateTime.now()}Z"
        )
    }

    private fun sendWebhook(payload: DiscordWebhookPayload) {
        try {
            webClient.post()
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String::class.java)
                .block()
        } catch (e: WebClientResponseException) {
            throw RuntimeException("Discord webhook failed with status: ${e.statusCode}, body: ${e.responseBodyAsString}", e)
        } catch (e: Exception) {
            throw RuntimeException("Discord webhook request failed", e)
        }
    }
}

data class DiscordWebhookPayload(
    val embeds: List<DiscordEmbed>,
)

data class DiscordEmbed(
    val title: String,
    val description: String,
    val color: Int,
    val fields: List<DiscordField>,
    val footer: DiscordFooter,
    val timestamp: String,
)

data class DiscordField(
    val name: String,
    val value: String,
    val inline: Boolean,
)

data class DiscordFooter(
    val text: String,
)
