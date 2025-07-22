package com.mandamong.server.mandalart.entity

import com.mandamong.server.mandalart.enums.Status
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.BatchSize

@Entity
@Table(name = "subjects")
class Subject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "subject")
    var subject: String,

    @JoinColumn(name = "mandalart_id")
    @OneToOne(fetch = FetchType.LAZY)
    val mandalart: Mandalart,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: Status = Status.IN_PROGRESS,

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "subject", cascade = [CascadeType.REMOVE], orphanRemoval = true, fetch = FetchType.LAZY)
    val objectives: MutableList<Objective> = mutableListOf()
) {

    companion object {
        fun of(subject: String, mandalart: Mandalart): Subject {
            return Subject(
                subject = subject,
                mandalart = mandalart,
            )
        }
    }

}
