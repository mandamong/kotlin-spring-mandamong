package com.mandamong.server.mandalart.event.handler

import com.mandamong.server.common.util.log.log
import com.mandamong.server.infrastructure.redis.CacheName
import com.mandamong.server.mandalart.event.dto.UpdateObjectiveEvent
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ObjectiveEventHandler {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @CacheEvict(cacheNames = [CacheName.MANDALART], key = "#event.mandalartId")
    fun handleObjectiveUpdated(event: UpdateObjectiveEvent) {
        log().info("CACHE_EVICT mandalartId=${event.mandalartId}")
    }

}

