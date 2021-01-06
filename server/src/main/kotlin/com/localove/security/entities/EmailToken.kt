package com.localove.security.entities

import com.localove.Identifiable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "email_tokens")
open class EmailToken: Identifiable() {
    @Column(name = "value")
    lateinit var value: UUID

    @ManyToOne
    @JoinColumn(name = "user_id")
    lateinit var user: User

    @Column(name = "creation_time")
    lateinit var creationTime: LocalDateTime
}

internal interface EmailTokenRepository: JpaRepository<EmailToken, Long> {
    fun findByValue(value: UUID): EmailToken?
}