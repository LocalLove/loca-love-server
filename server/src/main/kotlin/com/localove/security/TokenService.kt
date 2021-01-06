package com.localove.security

import com.localove.exceptions.InvalidTokenException
import com.localove.security.entities.Token
import com.localove.security.entities.TokenRepository
import com.localove.security.entities.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Service
class TokenService(
    @Value("\${loca-love.token.expiration}")
    private val expiration: Duration
) {
    @Transactional
    fun <T : Token> fillToken(tokenRepository: JpaRepository<T, Long>, user: User, token: T): T {
        token.apply {
            this.user = user
            value = UUID.randomUUID()
            creationTime = LocalDateTime.now()
        }

        return tokenRepository.save(token)
    }

    @Transactional
    fun <R : TokenRepository> validateToken(tokenRepository: R, token: String): User {
        val tokenValue = uuidFromString(token) ?: throw InvalidTokenException()
        val emailToken = tokenRepository.findByValue(tokenValue) ?: throw InvalidTokenException()
        val tokenLifetime = Duration.between(emailToken.creationTime, LocalDateTime.now())

        if (tokenLifetime >= expiration) {
            throw InvalidTokenException()
        }

        return emailToken.user
    }
}

private fun uuidFromString(src: String): UUID? {
    return try {
        UUID.fromString(src)
    } catch (e: IllegalArgumentException) {
        null
    }
}