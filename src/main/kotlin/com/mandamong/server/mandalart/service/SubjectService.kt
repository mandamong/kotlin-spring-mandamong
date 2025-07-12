package com.mandamong.server.mandalart.service

import com.mandamong.server.mandalart.entity.Subject
import com.mandamong.server.mandalart.repository.SubjectRepository
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
