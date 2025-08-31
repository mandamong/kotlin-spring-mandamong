package com.mandamong.server.infrastructure.discord.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class DiscordEmbed(
    val title: String,
    val description: String,
    val color: Int,
    val fields: List<DiscordField>,
    val footer: DiscordFooter,
    val timestamp: String,
) {

    companion object {
        fun of(e: Exception, requestInformation: String?): DiscordEmbed {
            val now = LocalDateTime.now()
            val timestamp = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val fields: List<DiscordField> = buildList {
                add(DiscordField("Exception Type", e.javaClass.simpleName, true))
                add(DiscordField("Message", e.message ?: "No message", true))
                add(DiscordField("Timestamp", timestamp, true))
                requestInformation?.let { add(DiscordField("Request Info", it, false)) }
                e.stackTrace.take(5).let { stackTrace ->
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
                color = RED,
                fields = fields,
                footer = DiscordFooter("Mandamong Server Alert"),
                timestamp = "${now}Z"
            )
        }

        private const val RED = 16711680
    }

}
