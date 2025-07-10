package com.mandamong.api.mandalart.dao

import com.mandamong.api.mandalart.domain.Objective
import org.springframework.data.jpa.repository.JpaRepository

interface ObjectiveRepository : JpaRepository<Objective, Long>
