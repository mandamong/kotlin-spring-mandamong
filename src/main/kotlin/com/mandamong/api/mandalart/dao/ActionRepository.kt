package com.mandamong.api.mandalart.dao

import com.mandamong.api.mandalart.domain.Action
import org.springframework.data.jpa.repository.JpaRepository

interface ActionRepository : JpaRepository<Action, Long>
