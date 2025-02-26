package com.mandamong.api.domains.mandalart.domain

import com.mandamong.api.domains.auth.domain.Member
import com.mandamong.api.domains.common.model.BaseTimeEntity
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
) : BaseTimeEntity()
