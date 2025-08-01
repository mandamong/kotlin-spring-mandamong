package com.mandamong.server.common.config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.springframework.beans.factory.DisposableBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoroutineConfig(
    private val coroutine: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
): DisposableBean {

    @Bean
    fun coroutineScope(): CoroutineScope = coroutine

    override fun destroy() = coroutine.cancel()

}

