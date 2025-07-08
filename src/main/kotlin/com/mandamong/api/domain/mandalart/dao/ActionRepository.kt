package com.mandamong.api.domain.mandalart.dao

import com.mandamong.api.domain.mandalart.domain.Action
import org.springframework.data.jpa.repository.JpaRepository

interface ActionRepository : JpaRepository<Action, Long>
