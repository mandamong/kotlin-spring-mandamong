package com.mandamong.server.mandalart.service

import com.mandamong.server.common.dto.PaginationParameter
import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.repository.MandalartRepository
import com.mandamong.server.user.entity.User
import com.mandamong.server.user.service.UserService
import kotlin.jvm.optionals.getOrNull
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MandalartService(
    private val repository: MandalartRepository,
    private val userService: UserService,
) {

    @Transactional
    fun create(name: String, userId: Long): Mandalart {
        val savedUser: User = userService.getById(userId)
        val mandalart = Mandalart(name = name, user = savedUser)
        return repository.save(mandalart)
    }

    @Transactional
    fun update(id: Long, updated: String): Mandalart {
        val mandalart = getById(id)
        mandalart.name = updated
        return mandalart
    }

    @Transactional
    fun deleteById(id: Long) = repository.deleteById(id)

    @Transactional(readOnly = true)
    fun findById(id: Long): Mandalart? = repository.findById(id).getOrNull()

    @Transactional(readOnly = true)
    fun getById(id: Long): Mandalart = findById(id) ?: throw IdNotFoundException(id)

    @Transactional(readOnly = true)
    fun findByIdWithFullData(id: Long): Mandalart? = repository.findByIdWithFullData(id)

    @Transactional(readOnly = true)
    fun getByIdWithFullData(id: Long): Mandalart = findByIdWithFullData(id) ?: throw IdNotFoundException(id)

    @Transactional(readOnly = true)
    fun findByUserIdWithPage(userId: Long, pageable: Pageable): Page<Mandalart>? =
        repository.findByUserId(userId, pageable)

    @Transactional(readOnly = true)
    fun getByUserIdWithPage(userId: Long, paginationParameter: PaginationParameter): Page<Mandalart> {
        val pageable = if (paginationParameter.number > 0) {
            PageRequest.of(paginationParameter.number - 1, paginationParameter.size)
        } else {
            PageRequest.of(0, paginationParameter.size)
        }
        return findByUserIdWithPage(userId, pageable) ?: throw IdNotFoundException(userId)
    }
}
