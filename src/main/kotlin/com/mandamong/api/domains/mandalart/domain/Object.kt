package com.mandamong.api.domains.mandalart.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "objects")
class Object(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0L,

    @Column(name = "object")
    var `object`: String,

    @ManyToOne
    @JoinColumn(name = "subject_id")
    var subject: Subject,
)
