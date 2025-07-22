package com.mandamong.server.common.util.jwt

import io.jsonwebtoken.Claims
import java.util.Date
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TokenUtilTest {

    @Autowired
    private lateinit var tokenUtil: TokenUtil

    private val memberId = 1L

    @Test
    fun `AccessToken 발급에 성공한다`() {
        val accessToken = tokenUtil.generateAccessToken(memberId)
        val claims: Claims = tokenUtil.parseAccessToken(accessToken)

        assertThat(accessToken).isNotNull()
        assertThat(claims.subject).isEqualTo(memberId.toString())
        assertThat(claims.expiration).isAfter(Date())
    }

    @Test
    fun `RefreshToken 발급에 성공한다`() {
        val refreshToken = tokenUtil.generateRefreshToken(memberId)
        val claims: Claims = tokenUtil.parseRefreshToken(refreshToken)

        assertThat(refreshToken).isNotNull()
        assertThat(claims.subject).isEqualTo(memberId.toString())
        assertThat(claims.expiration).isAfter(Date())
    }

}
