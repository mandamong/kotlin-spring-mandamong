package com.mandamong.server.mandalart.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "actions")
class Action(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "action")
    var action: String,

    @JoinColumn(name = "objective_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val objective: Objective,
) {
    companion object {
        fun of(action: String, objective: Objective): Action {
            return Action(
                action = action,
                objective = objective,
            )
        }
    }
}
