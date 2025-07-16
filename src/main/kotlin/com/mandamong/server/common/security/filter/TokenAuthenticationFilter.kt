package com.mandamong.server.common.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.mandamong.server.common.error.ErrorCode
import com.mandamong.server.common.error.ErrorResponse
import com.mandamong.server.common.util.jwt.JwtUtil
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TokenAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {

    val log = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val accessToken: String? = request.getHeader(AUTHORIZATION_HEADER)
            ?.takeIf { it.startsWith(TOKEN_PREFIX) }
            ?.substring(TOKEN_PREFIX.length)

        accessToken?.let {
            try {
                SecurityContextHolder.getContext().authentication = jwtUtil.getAuthentication(it)
            } catch (e: ExpiredJwtException) {
                log.error("ExpiredJwtException", e)
                response.status = ErrorCode.EXPIRED_TOKEN.status.value()
                response.contentType = "application/json"
                response.characterEncoding = "UTF-8"
                response.writer.write(objectMapper.writeValueAsString(ErrorResponse.of(ErrorCode.EXPIRED_TOKEN)))
                return
            }
        }

        filterChain.doFilter(request, response)
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val TOKEN_PREFIX = "Bearer "
    }

}
