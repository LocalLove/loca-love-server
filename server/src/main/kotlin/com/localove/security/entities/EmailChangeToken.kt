package com.localove.security.entities

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "email_change_tokens")
class EmailChangeToken(

    @Column(name = "email")
    val email: String

): Token()

interface EmailChangeTokenRepository: TokenRepository, JpaRepository<EmailChangeToken, Long>