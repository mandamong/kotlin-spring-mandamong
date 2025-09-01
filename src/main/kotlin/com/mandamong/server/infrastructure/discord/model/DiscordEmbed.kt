package com.mandamong.server.infrastructure.discord.model

import java.time.Instant
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
        fun of(
            e: Throwable,
            requestInformation: String?,
        ): DiscordEmbed {
            val instant = Instant.now()
            val timestamp = DateTimeFormatter.ISO_INSTANT.format(instant)
            val fields: List<DiscordField> =
                buildList {
                    add(DiscordField("Exception Type", e.javaClass.simpleName, true))
                    add(DiscordField("Message", e.message ?: "No message", true))
                    requestInformation?.let { add(DiscordField("Request Info", it, false)) }
                    e.stackTrace.take(5).let { stackTrace ->
                        if (stackTrace.isNotEmpty()) {
                            val stackTraceString =
                                stackTrace.joinToString("\n") {
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
                timestamp = timestamp,
            )
        }

        private const val RED = 16711680
    }
}
