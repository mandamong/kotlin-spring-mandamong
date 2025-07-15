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
        val savedUser: User = userService.findById(user.userId)
        return repository.save(
            Mandalart(
                name = name,
                user = savedUser,
            )
        )
    }

    @Transactional
    fun getMandalarts(userId: Long): List<Mandalart> {
        return repository.findByUserId(userId)
    }

    @Transactional
    fun findById(mandalartId: Long): Mandalart {
        return repository.findById(mandalartId).orElseThrow()
    }

    @Transactional
    fun deleteById(mandalartId: Long) {
        repository.deleteById(mandalartId)
    }

    @Transactional
    fun updateName(mandalartId: Long, updated: String): String {
        val mandalart = repository.findById(mandalartId).orElseThrow()
        mandalart.name = updated
        return mandalart.name
    }

}
