package com.mandamong.server.common.annotation.lock

import com.mandamong.server.common.constants.AopOrder
import com.mandamong.server.common.error.exception.MaxWaitForLockException
import com.mandamong.server.common.util.parser.SpringELParser
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Order(value = AopOrder.LOCK)
@Component
class DistributedLockAspect(
    private val redissonClient: RedissonClient,
) {
    @Around("@annotation(DistributedLock)")
    fun lock(joinPoint: ProceedingJoinPoint): Any? {
        val signature: MethodSignature = joinPoint.signature as MethodSignature
        val distributedLock: DistributedLock = signature.method.getAnnotation(DistributedLock::class.java)

        val key: String = SpringELParser.getDynamicValue(signature.parameterNames, joinPoint.args, distributedLock.key)
        val lockKey = "$REDISSON_LOCK_PREFIX${distributedLock.name}:$key"
        val rLock: RLock = redissonClient.getLock(lockKey)
        val locked: Boolean =
            rLock.tryLock(
                distributedLock.maxWaitForLock,
                distributedLock.autoUnlockAfter,
                distributedLock.timeUnit,
            )

        if (!locked) {
            throw MaxWaitForLockException()
        }

        try {
            return joinPoint.proceed()
        } finally {
            rLock.unlock()
        }
    }

    companion object {
        private const val REDISSON_LOCK_PREFIX = "LOCK::"
    }
}
