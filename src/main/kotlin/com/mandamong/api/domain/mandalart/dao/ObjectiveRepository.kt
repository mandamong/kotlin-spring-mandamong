package com.mandamong.api.domain.mandalart.dao

import com.mandamong.api.domain.mandalart.domain.Objective
import org.springframework.data.jpa.repository.JpaRepository

interface ObjectiveRepository : JpaRepository<Objective, Long>
