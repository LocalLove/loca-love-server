package com.localove.security.entities

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "email_tokens")
class EmailToken: Token()

interface EmailTokenRepository: JpaRepository<EmailToken, Long> {
    fun findByValue(value: UUID): EmailToken?
}