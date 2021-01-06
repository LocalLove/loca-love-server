package com.localove.security.entities

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "email_change_tokens")
class EmailChangeToken(

    @Column(name = "email")
    val email: String,

    value: UUID, user: User, creationTime: LocalDateTime

): EmailToken(value, user, creationTime)

interface EmailChangeTokenRepository: JpaRepository<EmailChangeToken, Long> {
    fun findByValue(value: UUID): EmailChangeToken?
}