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
@Table(name = "objectives")
class Objective(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0L,

    @Column(name = "objective")
    var objective: String,

    @JoinColumn(name = "subject_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var subject: Subject,
) {

    companion object {
        fun of(objective: String, subject: Subject): Objective {
            return Objective(
                objective = objective,
                subject = subject,
            )
        }
    }
}
