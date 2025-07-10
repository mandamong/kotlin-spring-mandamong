package com.mandamong.api.mandalart.application

import com.mandamong.api.mandalart.dao.ActionRepository
import org.springframework.stereotype.Service

@Service
class ActionService(
    private val repository: ActionRepository,
) {

}
