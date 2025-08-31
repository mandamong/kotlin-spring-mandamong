package com.mandamong.server.infrastructure.discord.dto

import com.mandamong.server.infrastructure.discord.model.DiscordEmbed

data class DiscordPayload(
    val embeds: List<DiscordEmbed>,
)
