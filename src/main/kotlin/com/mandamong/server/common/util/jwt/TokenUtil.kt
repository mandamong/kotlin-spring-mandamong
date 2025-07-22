package com.mandamong.server.common.util.jwt

import com.mandamong.server.user.dto.LoginUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Base64
import java.util.Date
import javax.crypto.SecretKey
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component

@Component
class TokenUtil(
    private val properties: TokenProperties,
) {

    private val rawSecretKey: ByteArray = properties.secretKey.toByteArray()
    private val decodedSecretKey: ByteArray = Base64.getDecoder().decode(properties.secretKey)

    private val accessSignKey: SecretKey = Keys.hmacShaKeyFor(rawSecretKey)
    private val refreshSignKey: SecretKey = Keys.hmacShaKeyFor(decodedSecretKey)

    fun generateAccessToken(userId: Long): String {
        val now = Date()
        val expiry = Date(now.time + properties.accessExpiry)
        return Jwts.builder()
            .header()
            .type(TOKEN_TYPE)
            .and()
            .subject(userId.toString())
            .issuedAt(now)
            .notBefore(now)
            .expiration(expiry)
            .signWith(accessSignKey)
            .compact()
    }

    fun generateRefreshToken(userId: Long): String {
        val now = Date()
        val expiry = Date(now.time + properties.refreshExpiry)
        return Jwts.builder()
            .header()
            .type(TOKEN_TYPE)
            .and()
            .subject(userId.toString())
            .issuedAt(now)
            .notBefore(now)
            .expiration(expiry)
            .signWith(refreshSignKey)
            .compact()
    }

    fun parseAccessToken(accessToken: String): Claims {
        return Jwts.parser()
            .verifyWith(accessSignKey)
            .build()
            .parseSignedClaims(accessToken)
            .payload
    }

    fun parseRefreshToken(refreshToken: String): Claims {
        return Jwts.parser()
            .verifyWith(refreshSignKey)
            .build()
            .parseSignedClaims(refreshToken)
            .payload
    }

    fun getAuthentication(accessToken: String): UsernamePasswordAuthenticationToken {
        val claims = parseAccessToken(accessToken)
        val userId = claims.subject.toLong()
        val principal = LoginUser(userId)
        return UsernamePasswordAuthenticationToken(principal, null, emptyList())
    }

    fun getRefreshExpiry(): Long = properties.refreshExpiry

    companion object {
        private const val TOKEN_TYPE = "JWT"
    }

}
