package com.localove.security.entities

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "email_tokens")
class EmailToken: Token()

interface EmailTokenRepository: TokenRepository, JpaRepository<EmailToken, Long>