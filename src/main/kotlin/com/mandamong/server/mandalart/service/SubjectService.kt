package com.mandamong.server.mandalart.service

import com.mandamong.server.common.error.exception.IdNotFoundException
import com.mandamong.server.mandalart.dto.MandalartUpdateRequest
import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.entity.Subject
import com.mandamong.server.mandalart.repository.SubjectRepository
import kotlin.jvm.optionals.getOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubjectService(
    private val repository: SubjectRepository,
) {

    @Transactional
    fun create(subjectName: String, mandalart: Mandalart): Subject {
        val subject = Subject.of(subjectName, mandalart)
        return repository.save(subject)
    }

    @Transactional
    fun update(id: Long, request: MandalartUpdateRequest): MandalartUpdateRequest {
        val subject = getById(id)
        subject.subject = request.updated
        return MandalartUpdateRequest(updated = subject.subject)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Subject? = repository.findById(id).getOrNull()

    @Transactional(readOnly = true)
    fun getById(id: Long): Subject = findById(id) ?: throw IdNotFoundException(id)

}
