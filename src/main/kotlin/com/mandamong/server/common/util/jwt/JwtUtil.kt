package com.mandamong.server.common.util.jwt

import com.mandamong.server.infrastructure.redis.RedisService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Base64
import java.util.Collections
import java.util.Date
import javax.crypto.SecretKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.access-expiration}") private val accessExpiration: Long,
    @Value("\${jwt.refresh-expiration}") private val refreshExpiration: Long,
    private val redisService: RedisService,
) {

    private val rawSecretKey: ByteArray = secretKey.toByteArray()
    private val decodedSecretKey: ByteArray = Base64.getDecoder().decode(secretKey)
    private val accessSignKey: SecretKey = Keys.hmacShaKeyFor(rawSecretKey)
    private val refreshSignKey: SecretKey = Keys.hmacShaKeyFor(decodedSecretKey)

    fun generateAccessToken(memberId: Long): String {
        val now = Date()
        return Jwts.builder()
            .header()
            .type(TOKEN_TYPE)
            .and()
            .subject(memberId.toString())
            .issuedAt(now)
            .notBefore(now)
            .expiration(Date(now.time + accessExpiration))
            .signWith(accessSignKey)
            .compact()
    }

    fun generateRefreshToken(memberId: Long): String {
        val now = Date()
        return Jwts.builder()
            .header()
            .type(TOKEN_TYPE)
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

    fun validateMemberIdInAccessToken(accessToken: String) {
        val memberId = parseAccessToken(accessToken).subject.toLong()
        val savedAccessToken: String = redisService.get(memberId.toString())!!
        if (accessToken != savedAccessToken) {
            throw IllegalStateException("Access Token, Member Id 검증 오류")
        }
    }

    fun getAuthentication(accessToken: String): UsernamePasswordAuthenticationToken {
        val claims = parseAccessToken(accessToken)
        val authorities = Collections.singleton(SimpleGrantedAuthority("ROLE_USER"))
        return UsernamePasswordAuthenticationToken(User(claims.subject, "", authorities), accessToken, authorities)
    }

    companion object {
        private const val TOKEN_TYPE = "JWT"
    }

}
