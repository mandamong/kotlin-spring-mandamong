package com.mandamong.server.mandalart.service

import com.mandamong.server.common.error.exception.UnauthorizedException
import com.mandamong.server.common.util.log.log
import com.mandamong.server.mandalart.entity.MandalartTag
import com.mandamong.server.mandalart.repository.MandalartTagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MandalartTagService(
    private val mandalartTagRepository: MandalartTagRepository,
    private val mandalartService: MandalartService,
    private val tagService: TagService,
) {
    private val log = log()

    @Transactional
    fun addTagToMandalart(
        mandalartId: Long,
        tagId: Long,
        userId: Long,
    ): MandalartTag {
        val mandalart = mandalartService.getById(mandalartId)
        val tag = tagService.getById(tagId)
        
        // 소유권 검증
        validateMandalartOwnership(mandalart.user.id, userId)
        validateTagOwnership(tag.user.id, userId)
        
        // 이미 연결되어 있는지 확인
        if (mandalartTagRepository.existsByMandalartAndTag(mandalart, tag)) {
            throw IllegalArgumentException("Tag is already added to mandalart")
        }
        
        val mandalartTag = MandalartTag.of(mandalart, tag)
        val savedMandalartTag = mandalartTagRepository.save(mandalartTag)
        log.info("ADD_TAG_TO_MANDALART mandalartId=$mandalartId tagId=$tagId userId=$userId")
        return savedMandalartTag
    }

    @Transactional
    fun removeTagFromMandalart(
        mandalartId: Long,
        tagId: Long,
        userId: Long,
    ) {
        val mandalart = mandalartService.getById(mandalartId)
        val tag = tagService.getById(tagId)
        
        // 소유권 검증
        validateMandalartOwnership(mandalart.user.id, userId)
        validateTagOwnership(tag.user.id, userId)
        
        mandalartTagRepository.deleteByMandalartIdAndTagId(mandalartId, tagId)
        log.info("REMOVE_TAG_FROM_MANDALART mandalartId=$mandalartId tagId=$tagId userId=$userId")
    }

    private fun validateMandalartOwnership(
        mandalartOwnerId: Long,
        userId: Long,
    ) {
        if (mandalartOwnerId != userId) {
            throw UnauthorizedException(message = "Mandalart does not belong to user")
        }
    }

    private fun validateTagOwnership(
        tagOwnerId: Long,
        userId: Long,
    ) {
        if (tagOwnerId != userId) {
            throw UnauthorizedException(message = "Tag does not belong to user")
        }
    }
}
