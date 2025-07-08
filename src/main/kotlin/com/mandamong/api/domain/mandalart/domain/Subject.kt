package com.mandamong.api.domain.mandalart.domain

import com.mandamong.api.domain.auth.domain.Member
import com.mandamong.api.global.entity.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "subjects")
class Subject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0L,

    @Column(name = "subject")
    var subject: String,

    @ManyToOne
    @JoinColumn(name = "member_id")
    var member: Member,
) : BaseTimeEntity() {

    /*fun from(subject: String): Subject {
        return Subject()
    }*/
}
