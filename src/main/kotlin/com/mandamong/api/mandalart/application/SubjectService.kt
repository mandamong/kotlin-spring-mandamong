package com.mandamong.api.mandalart.application

import com.mandamong.api.mandalart.dao.SubjectRepository
import com.mandamong.api.mandalart.domain.Subject
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubjectService(
    private val repository: SubjectRepository,
) {

    @Transactional
    fun save(subject: Subject) {
        repository.save(subject)
    }
}
