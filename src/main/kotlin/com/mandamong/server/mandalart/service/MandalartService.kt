package com.mandamong.server.mandalart.service

import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.repository.MandalartRepository
import com.mandamong.server.user.dto.AuthenticatedUser
import com.mandamong.server.user.entity.User
import com.mandamong.server.user.service.UserService
import kotlin.jvm.optionals.getOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MandalartService(
    private val repository: MandalartRepository,
    private val userService: UserService,
) {

    @Transactional
    fun create(name: String, loginUser: AuthenticatedUser): Mandalart {
        val savedUser: User = userService.getById(loginUser.userId)
        return repository.save(Mandalart(name = name, user = savedUser))
    }

    @Transactional
    fun update(id: Long, updated: String): String {
        val mandalart = getById(id)
        mandalart.name = updated
        return mandalart.name
    }

    @Transactional
    fun findById(id: Long): Mandalart? = repository.findById(id).getOrNull()

    @Transactional
    fun getById(id: Long): Mandalart = findById(id) ?: throw IdNotFoundException(id)

    @Transactional
    fun deleteById(id: Long) = repository.deleteById(id)

    @Transactional
    fun findByUserId(userId: Long): List<Mandalart>? = repository.findByUserId(userId)

    @Transactional
    fun getByUserId(userId: Long): List<Mandalart> = findByUserId(userId) ?: throw IdNotFoundException(userId)

}
