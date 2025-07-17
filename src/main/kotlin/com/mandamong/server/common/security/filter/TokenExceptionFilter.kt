package com.mandamong.server.common.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.mandamong.server.common.error.ErrorCode
import com.mandamong.server.common.error.ErrorResponse
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TokenExceptionFilter(
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {

    val log = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
    ) {
        try {
            chain.doFilter(request, response)
        } catch (e: Exception) {
            log.error("Exception", e)
            handle(e, response)
        }
    }

    private fun handle(e: Exception, response: HttpServletResponse) {
        val errorCode: ErrorCode = when (e) {
            is ExpiredJwtException -> ErrorCode.EXPIRED_TOKEN
            is MalformedJwtException, is SignatureException -> ErrorCode.INVALID_TOKEN
            is UnsupportedJwtException -> ErrorCode.UNSUPPORTED_TOKEN
            is IllegalArgumentException -> ErrorCode.TOKEN_NOT_FOUND
            else -> ErrorCode.INTERNAL_SERVER_ERROR
        }

        response.status = errorCode.status.value()
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        val errorResponse = ErrorResponse.of(errorCode)
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }

}
