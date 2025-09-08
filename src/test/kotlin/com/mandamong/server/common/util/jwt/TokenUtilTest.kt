package com.mandamong.server.common.util.jwt

import io.jsonwebtoken.Claims
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Date

class TokenUtilTest {
    private val properties: TokenProperties = TokenProperties(issuer = ISSUER, secret = SECRET, expiry = EXPIRY)
    private val tokenUtil = TokenUtil(properties)

    @Test
    fun `AccessToken 발급에 성공한다`() {
        val accessToken = tokenUtil.createAccessToken(USER_ID)
        val claims: Claims = tokenUtil.parseAccessToken(accessToken)

        assertThat(accessToken).isNotNull()
        assertThat(claims.subject).isEqualTo(USER_ID.toString())
        assertThat(claims.expiration).isAfter(Date())
    }

    @Test
    fun `RefreshToken 발급에 성공한다`() {
        val refreshToken = tokenUtil.createRefreshToken(USER_ID)
        val claims: Claims = tokenUtil.parseRefreshToken(refreshToken)

        assertThat(refreshToken).isNotNull()
        assertThat(claims.subject).isEqualTo(USER_ID.toString())
        assertThat(claims.expiration).isAfter(Date())
    }

    companion object {
        private const val USER_ID = 1L
        private const val ISSUER = "test-issuer"
        private const val SECRET = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret"
        private val EXPIRY = TokenProperties.Expiry(access = 3600, refresh = 7200)
    }
}
