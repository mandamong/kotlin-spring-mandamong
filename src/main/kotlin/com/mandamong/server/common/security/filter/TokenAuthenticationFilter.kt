package com.mandamong.server.common.security.filter

import com.mandamong.server.common.util.jwt.TokenUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TokenAuthenticationFilter(
    private val tokenUtil: TokenUtil,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
    ) {
        val accessToken: String? = request.getHeader(AUTHORIZATION_HEADER)
            ?.takeIf { it.startsWith(TOKEN_PREFIX) }
            ?.substring(TOKEN_PREFIX.length)

        accessToken?.let { SecurityContextHolder.getContext().authentication = tokenUtil.getAuthentication(accessToken) }

        chain.doFilter(request, response)
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val TOKEN_PREFIX = "Bearer "
    }

}
