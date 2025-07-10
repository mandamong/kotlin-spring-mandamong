package com.mandamong.api.mandalart.dao

import com.mandamong.api.mandalart.domain.Subject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubjectRepository : JpaRepository<Subject, Long>
