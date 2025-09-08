package com.mandamong.server.mandalart.entity

import com.mandamong.server.common.entity.BaseTimeEntity
import com.mandamong.server.user.entity.User
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(
    name = "tags", 
    indexes = [
        Index(name = "tags_user_id_index", columnList = "user_id"),
        Index(name = "tags_name_user_id_index", columnList = "name, user_id", unique = true)
    ]
)
class Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(name = "name", nullable = false, length = 50)
    var name: String,
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,
    @OneToMany(mappedBy = "tag", cascade = [CascadeType.REMOVE], orphanRemoval = true, fetch = FetchType.LAZY)
    val mandalartTags: MutableList<MandalartTag> = mutableListOf(),
) : BaseTimeEntity() {
    
    companion object {
        fun of(
            name: String,
            user: User,
        ): Tag = Tag(
            name = name,
            user = user,
        )
    }
    
}
