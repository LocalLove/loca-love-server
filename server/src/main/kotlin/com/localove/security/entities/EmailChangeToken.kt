package com.localove.security.entities

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "email_change_tokens")
class EmailChangeToken(

    @Column(name = "email")
    val email: String

): EmailToken()

interface EmailChangeTokenRepository: JpaRepository<EmailChangeToken, Long> {
    fun findByValue(value: UUID): EmailChangeToken?
}