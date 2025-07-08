package com.mandamong.api.domain.mandalart.application

import com.mandamong.api.domain.mandalart.dao.ObjectiveRepository
import org.springframework.stereotype.Service

@Service
class ObjectiveService(
    private val repository: ObjectiveRepository,
) {
}
