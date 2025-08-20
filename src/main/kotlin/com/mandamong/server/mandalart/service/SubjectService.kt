package com.mandamong.server.mandalart.service

import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.infrastructure.redis.CacheName
import com.mandamong.server.mandalart.dto.UpdateSubjectResponse
import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.entity.Subject
import com.mandamong.server.mandalart.repository.SubjectRepository
import kotlin.jvm.optionals.getOrNull
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubjectService(
    private val repository: SubjectRepository,
) {

    @Transactional
    @CacheEvict(cacheNames = [CacheName.MANDALARTS], key = "#userId")
    fun create(subject: String, mandalart: Mandalart, userId: Long): Subject {
        return repository.save(Subject.of(subject, mandalart))
    }

    @Transactional
    @CacheEvict(cacheNames = [CacheName.MANDALARTS], key = "#userId")
    fun update(id: Long, newSubject: String, userId: Long): UpdateSubjectResponse {
        val subject = getById(id)
        subject.subject = newSubject
        return UpdateSubjectResponse.of(subject)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Subject? = repository.findById(id).getOrNull()

    @Transactional(readOnly = true)
    fun getById(id: Long): Subject = findById(id) ?: throw IdNotFoundException(id)

}
