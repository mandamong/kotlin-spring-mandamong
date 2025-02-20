package com.mandamong.api.global.config

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig(
    @Value("\${minio.endpoint}")
    private val endpoint: String,

    @Value("\${minio.accessKey}")
    private val accessKey: String,

    @Value("\${minio.secretKey}")
    private val secretKey: String,
) {
    @Bean
    fun minioClient(): MinioClient {
        return MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build()
    }
}
