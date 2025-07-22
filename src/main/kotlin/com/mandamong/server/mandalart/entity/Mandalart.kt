package com.mandamong.server.mandalart.entity

import com.mandamong.server.common.entity.BaseTimeEntity
import com.mandamong.server.mandalart.enums.Status
import com.mandamong.server.user.entity.User
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
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "mandalarts")
class Mandalart(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "name")
    var name: String,

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: Status = Status.IN_PROGRESS,

    @OneToOne(mappedBy = "mandalart", cascade = [CascadeType.REMOVE], orphanRemoval = true, fetch = FetchType.LAZY)
    val subject: Subject? = null
) : BaseTimeEntity()
