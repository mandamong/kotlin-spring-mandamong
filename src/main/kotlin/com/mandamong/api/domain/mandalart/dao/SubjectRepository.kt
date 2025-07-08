package com.mandamong.api.domain.mandalart.dao

import com.mandamong.api.domain.mandalart.domain.Subject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubjectRepository : JpaRepository<Subject, Long>
