package com.mandamong.server.mandalart.entity

import com.mandamong.server.common.entity.BaseTimeEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

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
    @ManyToOne(fetch = FetchType.LAZY)
    val mandalart: Mandalart,

    @OneToMany(mappedBy = "subject", cascade = [CascadeType.REMOVE], orphanRemoval = true, fetch = FetchType.LAZY)
    val objectives: MutableList<Objective> = mutableListOf()
) : BaseTimeEntity() {

    companion object {
        fun of(subject: String, mandalart: Mandalart): Subject {
            return Subject(
                subject = subject,
                mandalart = mandalart,
            )
        }
    }

}
