package com.mandamong.api.infrastructure.application

import java.time.Duration
import java.util.concurrent.TimeUnit
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RedisService(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    fun setValues(key: String, data: String) {
        val values = redisTemplate.opsForValue()
        values[key] = data
    }

    fun setValues(key: String, data: String, duration: Duration) {
        val values = redisTemplate.opsForValue()
        values.set(key, data, duration)
    }

    @Transactional(readOnly = true)
    fun getValues(key: String): String {
        val values = redisTemplate.opsForValue()
        if (values[key] == null) {
            return "false"
        }
        return values[key].toString()
    }

    fun deleteValues(key: String) {
        redisTemplate.delete(key)
    }

    fun expireValues(key: String, timeout: Int) {
        redisTemplate.expire(key, timeout.toLong(), TimeUnit.MILLISECONDS)
    }

    fun setHashOps(key: String, data: Map<String, String>) {
        val values = redisTemplate.opsForHash<Any, Any>()
        values.putAll(key, data)
    }

    @Transactional(readOnly = true)
    fun getHashOps(key: String, hashKey: String): String {
        val values = redisTemplate.opsForHash<Any, Any>()
        return values.get(key, hashKey).toString()
    }

    fun deleteHashOps(key: String, hashKey: String) {
        val values = redisTemplate.opsForHash<Any, Any>()
        values.delete(key, hashKey)
    }

    fun checkExistsValue(value: String): Boolean {
        return value != "false"
    }
}
