package com.localove.security.entities

import com.localove.Identifiable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MappedSuperclass

@MappedSuperclass
open class Token: Identifiable() {
    @Column(name = "value")
    lateinit var value: UUID

    @ManyToOne
    @JoinColumn(name = "user_id")
    lateinit var user: User

    @Column(name = "creation_time")
    lateinit var creationTime: LocalDateTime
}