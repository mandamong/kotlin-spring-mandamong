package com.mandamong.server.common.annotation.lock

import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DistributedLock(
    val name: String,
    val key: String,
    val maxWaitForLock: Long = 5L,
    val autoUnlockAfter: Long = 3L,
    val timeUnit: TimeUnit = TimeUnit.SECONDS,
)
