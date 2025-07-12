package com.mandamong.server.infrastructure.redis

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
class RedisConfig(
    @Value(value = "\${spring.data.redis.host}") private val host: String,
    @Value(value = "\${spring.data.redis.port}") private val port: Int,
    @Value(value = "\${spring.data.redis.password}") private val password: String,
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val configuration = RedisStandaloneConfiguration(host, port)
        configuration.setPassword(password)
        return LettuceConnectionFactory(configuration)
    }

}
