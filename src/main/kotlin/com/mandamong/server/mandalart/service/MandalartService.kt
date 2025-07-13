package com.mandamong.server.mandalart.service

import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.repository.MandalartRepository
import com.mandamong.server.user.dto.AuthenticatedUser
import com.mandamong.server.user.entity.User
import com.mandamong.server.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MandalartService(
    private val repository: MandalartRepository,
    private val userService: UserService,
) {

    @Transactional
    fun save(name: String, user: AuthenticatedUser): Mandalart {
        val savedUser: User = userService.findById(user.userId).orElseThrow()
        return repository.save(
            Mandalart(
                name = name,
                user = savedUser,
            )
        )
    }

}
