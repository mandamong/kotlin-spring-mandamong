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

    private val rawSecret: ByteArray = properties.secret.toByteArray()
    private val decodedSecret: ByteArray = Base64.getDecoder().decode(properties.secret)

    private val accessSignKey: SecretKey = Keys.hmacShaKeyFor(rawSecret)
    private val refreshSignKey: SecretKey = Keys.hmacShaKeyFor(decodedSecret)

    fun createAccessToken(userId: Long): String {
        val now = Date()
        val expiry = Date(now.time + properties.expiry.access)
        return Jwts.builder()
            .header()
            .type(TOKEN_TYPE)
            .and()
            .issuer(properties.issuer)
            .issuedAt(now)
            .notBefore(now)
            .expiration(expiry)
            .subject(userId.toString())
            .signWith(accessSignKey)
            .compact()
    }

    fun createRefreshToken(userId: Long): String {
        val now = Date()
        val expiry = Date(now.time + properties.expiry.refresh)
        return Jwts.builder()
            .header()
            .type(TOKEN_TYPE)
            .and()
            .issuer(properties.issuer)
            .issuedAt(now)
            .notBefore(now)
            .expiration(expiry)
            .subject(userId.toString())
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

    companion object {
        private const val TOKEN_TYPE = "JWT"
    }

}
