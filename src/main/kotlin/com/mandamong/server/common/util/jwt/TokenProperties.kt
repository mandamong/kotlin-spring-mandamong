package com.mandamong.server.common.util.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class TokenProperties(
    val secretKey: String,
    val accessExpiry: Long,
    val refreshExpiry: Long,
)
