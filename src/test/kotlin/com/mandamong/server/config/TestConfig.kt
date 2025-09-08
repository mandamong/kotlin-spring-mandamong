package com.mandamong.server.config

import com.mandamong.server.infrastructure.minio.service.MinioService
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@TestConfiguration
@Profile("test")
class TestConfig {

    @Bean
    @Primary
    fun minioService(): MinioService {
        return Mockito.mock(MinioService::class.java)
    }
}
