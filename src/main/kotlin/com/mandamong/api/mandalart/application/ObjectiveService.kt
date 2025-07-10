package com.mandamong.api.mandalart.application

import com.mandamong.api.mandalart.dao.ObjectiveRepository
import org.springframework.stereotype.Service

@Service
class ObjectiveService(
    private val repository: ObjectiveRepository,
) {
}
