package com.mandamong.server.mandalart.service

import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.common.error.exception.UnauthorizedException
import com.mandamong.server.common.util.log.log
import com.mandamong.server.mandalart.entity.Tag
import com.mandamong.server.mandalart.repository.TagRepository
import com.mandamong.server.user.entity.User
import com.mandamong.server.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class TagService(
    private val tagRepository: TagRepository,
    private val userService: UserService,
) {
    private val log = log()

    @Transactional
    fun create(
        name: String,
        userId: Long,
    ): Tag {
        val user: User = userService.getById(userId)
        val tag = Tag.of(name = name, user = user)
        val savedTag = tagRepository.save(tag)
        log.info("CREATE_TAG tagId=${savedTag.id} userId=$userId name=$name")
        return savedTag
    }

    @Transactional
    fun delete(
        tagId: Long,
        userId: Long,
    ) {
        val tag = getById(tagId)
        validateTagOwnership(tag, userId)
        tagRepository.deleteById(tagId)
        log.info("DELETE_TAG tagId=$tagId userId=$userId")
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Tag? = tagRepository.findById(id).getOrNull()

    @Transactional(readOnly = true)
    fun getById(id: Long): Tag = findById(id) ?: throw IdNotFoundException(id)

    @Transactional(readOnly = true)
    fun findByUser(userId: Long): List<Tag> {
        val user = userService.getById(userId)
        return tagRepository.findByUserOrderByCreatedAtDesc(user)
    }

    @Transactional(readOnly = true)
    fun searchByName(
        userId: Long,
        keyword: String,
    ): List<Tag> {
        val user = userService.getById(userId)
        return tagRepository.findByUserAndNameContaining(user, keyword)
    }

    private fun validateTagOwnership(
        tag: Tag,
        userId: Long,
    ) {
        if (tag.user.id != userId) {
            throw UnauthorizedException(message = "Tag does not belong to user")
        }
    }
}
