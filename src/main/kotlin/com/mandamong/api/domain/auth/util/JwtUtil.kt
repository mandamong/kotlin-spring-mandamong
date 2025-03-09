package com.mandamong.api.domain.auth.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
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

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val TOKEN_PREFIX = "Bearer "
    }

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

    fun validateMemberIdInAccessToken(accessToken: String, memberId: Long): Boolean {
        val claims: Claims = parseAccessToken(accessToken)
        val memberIdInToken: String = claims.subject
        if (memberIdInToken == memberId.toString()) {
            return true
        }
        throw IllegalStateException("Access Token, Member Id 검증 오류")
    }

    fun validateMemberIdInRefreshToken(refreshToken: String, memberId: Long): Boolean {
        val claims: Claims = parseRefreshToken(refreshToken)
        val memberIdInToken: String = claims.subject
        if (memberIdInToken == memberId.toString()) {
            return true
        }
        throw IllegalStateException("Access Token, Member Id 검증 오류")
    }

    fun resolve(request: HttpServletRequest): String? {
        return request.getHeader(AUTHORIZATION_HEADER)
            .takeIf { it.startsWith(TOKEN_PREFIX) }
            ?.substring(TOKEN_PREFIX.length)
    }
}
