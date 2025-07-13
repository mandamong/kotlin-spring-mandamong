package com.mandamong.server.mandalart.service

import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.entity.Subject
import com.mandamong.server.mandalart.repository.SubjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubjectService(
    private val repository: SubjectRepository,
) {

    @Transactional
    fun save(subjectName: String, mandalart: Mandalart): Subject {
        val subject = Subject.of(subjectName, mandalart)
        return repository.save(subject)
    }

}
