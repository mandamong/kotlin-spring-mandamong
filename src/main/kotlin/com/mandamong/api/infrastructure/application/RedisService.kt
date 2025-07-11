package com.mandamong.api.infrastructure.application

import java.time.Duration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RedisService(
    private val redisTemplate: StringRedisTemplate,
) {

    fun set(key: String, data: String, duration: Duration) {
        val redis = redisTemplate.opsForValue()
        redis.set(key, data, duration)
    }

    @Transactional(readOnly = true)
    fun get(key: String): String? {
        val redis = redisTemplate.opsForValue()
        if (redis.get(key) == null) {
            return null
        }
        return redis.get(key)
    }

    fun delete(key: String) {
        redisTemplate.delete(key)
    }

    fun isExist(key: String): Boolean {
        return redisTemplate.opsForValue().get(key) != null
    }
}
