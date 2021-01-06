package com.localove.security

import com.localove.security.entities.EmailToken
import com.localove.security.entities.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Service
class TokenService(
    private val clock: Clock,

    @Value("\${loca-love.token.expiration}")
    private val duration: Duration
) {
    @Transactional
    fun <T : EmailToken> fillToken(tokenRepository: JpaRepository<T, Long>, user: User, token: T): T {
        token.apply {
            this.user = user
            value = UUID.randomUUID()
            creationTime = LocalDateTime.now()
        }

        return tokenRepository.save(token)
    }
}