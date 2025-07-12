package com.mandamong.server.mandalart.service

import com.mandamong.server.mandalart.repository.ActionRepository
import org.springframework.stereotype.Service

@Service
class ActionService(
    private val repository: ActionRepository,
) {

}
