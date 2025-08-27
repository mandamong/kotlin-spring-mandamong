package com.mandamong.server.common.notification.discord

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class DiscordWebhookService(
    private val objectMapper: ObjectMapper,
) {

    private val log = LoggerFactory.getLogger(DiscordWebhookService::class.java)

    @Value("\${discord.webhook.url:}")
    private lateinit var webhookUrl: String

    @Value("\${discord.webhook.enabled:true}")
    private var webhookEnabled: Boolean = true

    private val httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build()

    @Async
    fun sendErrorNotification(exception: Exception, requestInfo: String? = null) {
        if (!webhookEnabled || webhookUrl.isBlank()) {
            log.warn("Discord webhook is disabled or URL is not configured")
            return
        }

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
        val json = objectMapper.writeValueAsString(payload)

        val request = HttpRequest.newBuilder()
            .uri(URI.create(webhookUrl))
            .header("Content-Type", "application/json")
            .timeout(Duration.ofSeconds(30))
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() !in 200..299) {
            throw RuntimeException("Discord webhook failed with status: ${response.statusCode()}, body: ${response.body()}")
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
