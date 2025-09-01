package com.mandamong.server.common.util.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class TokenProperties(
    val issuer: String,
    val secret: String,
    val expiry: Expiry,
) {
    class Expiry(
        val access: Long,
        val refresh: Long,
    )
}
