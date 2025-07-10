package com.mandamong.api.auth.util

import io.jsonwebtoken.Claims
import java.util.Date
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtUtilTest {
    private val logger: Logger = LoggerFactory.getLogger(JwtUtilTest::class.java)

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Test
    fun `Access Token 발급에 성공한다`() {
        val memberId: Long = 1L

        val accessToken = jwtUtil.generateAccessToken(memberId)

        assertThat(accessToken).isNotNull()

        val claims: Claims = jwtUtil.parseAccessToken(accessToken)

        assertThat(memberId.toString()).isEqualTo(claims.subject)
        assertThat(claims.expiration.after(Date())).isTrue()
        logger.info(accessToken)
    }

    @Test
    fun `Refresh Token 발급에 성공한다`() {
        val memberId: Long = 1L

        val refreshToken = jwtUtil.generateRefreshToken(memberId)

        assertThat(refreshToken).isNotNull()

        val claims: Claims = jwtUtil.parseRefreshToken(refreshToken)

        assertThat(memberId.toString()).isEqualTo(claims.subject)
        assertThat(claims.expiration.after(Date())).isTrue()
        logger.info(refreshToken)
    }
}
