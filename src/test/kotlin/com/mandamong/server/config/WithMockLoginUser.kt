package com.mandamong.server.config

import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockLoginUserSecurityContextFactory::class)
annotation class WithMockLoginUser(
    val userId: Long = 1L
)
