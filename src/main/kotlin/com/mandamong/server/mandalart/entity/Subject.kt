package com.mandamong.server.mandalart.entity

import com.mandamong.server.common.entity.BaseTimeEntity
import com.mandamong.server.user.entity.User
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
    @JoinColumn(name = "user_id")
    var user: User,
) : BaseTimeEntity() {

}
