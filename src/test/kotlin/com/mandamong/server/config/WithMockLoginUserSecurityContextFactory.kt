package com.mandamong.server.config

import com.mandamong.server.user.dto.LoginUser
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockLoginUserSecurityContextFactory : WithSecurityContextFactory<WithMockLoginUser> {
    
    override fun createSecurityContext(annotation: WithMockLoginUser): SecurityContext {
        val principal = LoginUser(userId = annotation.userId)
        val authentication = UsernamePasswordAuthenticationToken(principal, null, emptyList())
        
        val context = SecurityContextHolder.createEmptyContext()
        context.authentication = authentication
        return context
    }
}
