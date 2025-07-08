package com.mandamong.api.global.filter

import com.mandamong.api.domain.auth.util.JwtUtil
import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TokenAuthenticationFilter(
    private val jwtUtil: JwtUtil,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val header: String? = request.getHeader(AUTHORIZATION_HEADER)
        val token: String? = header
            ?.takeIf { it.startsWith(TOKEN_PREFIX) }
            ?.substring(TOKEN_PREFIX.length)

        token?.let {
            val claims: Claims = jwtUtil.parseAccessToken(it)
            val memberId: Long = claims.subject.toLong()
            if (jwtUtil.validateMemberIdInAccessToken(it, memberId)) {
                val authentication = jwtUtil.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            }

        }

        filterChain.doFilter(request, response)
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val TOKEN_PREFIX = "Bearer "
    }
}
