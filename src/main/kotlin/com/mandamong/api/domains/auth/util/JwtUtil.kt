package com.mandamong.api.domains.auth.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Base64
import java.util.Date
import javax.crypto.SecretKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtUtil(
    @Value("\${jwt.secret}")
    private val secretKey: String,

    @Value("\${jwt.access-expiration}")
    private val accessExpiration: Long,

    @Value("\${jwt.refresh-expiration}")
    private val refreshExpiration: Long,
) {
    private val rawSecretKey: ByteArray = secretKey.toByteArray()
    private val decodedSecretKey: ByteArray = Base64.getDecoder().decode(secretKey)

    private val accessSignKey: SecretKey = Keys.hmacShaKeyFor(rawSecretKey)
    private val refreshSignKey: SecretKey = Keys.hmacShaKeyFor(decodedSecretKey)

    fun generateAccessToken(memberId: Long): String {
        val now: Date = Date()

        return Jwts.builder()
            .header()
            .type("JWT")
            .and()
            .subject(memberId.toString())
            .issuedAt(now)
            .notBefore(now)
            .expiration(Date(now.time + accessExpiration))
            .signWith(accessSignKey)
            .compact()
    }

    fun generateRefreshToken(memberId: Long): String {
        val now: Date = Date()

        return Jwts.builder()
            .header()
            .type("JWT")
            .and()
            .subject(memberId.toString())
            .issuedAt(now)
            .notBefore(now)
            .expiration(Date(now.time + refreshExpiration))
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

    fun validateRefreshToken(refreshToken: String, memberId: Long) {
        val claims: Claims = parseRefreshToken(refreshToken)
        val memberIdInToken: String = claims.subject
        if (isMemberIdNotEquals(memberIdInToken, memberId)) {
            throw IllegalStateException("Refresh Token 검증 오류")
        }
    }

    private fun isMemberIdNotEquals(memberIdInToken: String, memberId: Long): Boolean {
        return !memberIdInToken.equals(memberId.toString())
    }
}
