package com.mandamong.api.domain.mandalart.application

import com.mandamong.api.domain.mandalart.dao.ActionRepository
import org.springframework.stereotype.Service

@Service
class ActionService(
    private val repository: ActionRepository,
) {

}
