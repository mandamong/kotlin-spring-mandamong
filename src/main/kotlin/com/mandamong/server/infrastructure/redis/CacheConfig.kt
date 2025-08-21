package com.mandamong.server.infrastructure.redis

import java.time.Duration

enum class CacheConfig(
    val cacheName: String,
    val ttl: Duration,
) {
    MANDALART(CacheName.MANDALART, Duration.ofMinutes(5)),
    MANDALARTS(CacheName.MANDALARTS, Duration.ofMinutes(5)),
}
