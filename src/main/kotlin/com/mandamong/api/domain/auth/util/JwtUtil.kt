package com.mandamong.api.domain.auth.util

import com.mandamong.api.domain.auth.application.MemberService
import com.mandamong.api.infrastructure.application.RedisService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import java.util.Base64
import java.util.Collections
import java.util.Date
import javax.crypto.SecretKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component

@Component
class JwtUtil(
    @Value("\${jwt.secret}")
    private val secretKey: String,

    @Value("\${jwt.access-expiration}")
    private val accessExpiration: Long,

    @Value("\${jwt.refresh-expiration}")
    private val refreshExpiration: Long,

    private val redisService: RedisService,
    private val memberService: MemberService,
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

    fun validateMemberIdInAccessToken(accessToken: String, memberId: Long): Boolean {
        val token = redisService.getValues(memberId.toString())
        if (accessToken == token) {
            return true
        }
        throw IllegalStateException("Access Token, Member Id 검증 오류")
    }

    fun validateMemberIdInRefreshToken(refreshToken: String, memberId: Long): Boolean {
        val id = memberService.findByRefreshToken(refreshToken)
        if (memberId == id) {
            return true
        }
        throw IllegalStateException("Refresh Token, Member Id 검증 오류")
    }

    fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        val claims = parseAccessToken(token)
        val authorities = Collections.singleton(SimpleGrantedAuthority("ROLE_USER"))
        return UsernamePasswordAuthenticationToken(User(claims.subject, "", authorities), token, authorities)
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val TOKEN_PREFIX = "Bearer "
    }
}
