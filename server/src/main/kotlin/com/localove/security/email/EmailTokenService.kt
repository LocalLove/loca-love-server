package com.localove.security.email

import com.localove.security.entities.EmailToken
import com.localove.security.entities.EmailTokenRepository
import com.localove.security.entities.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Service
internal class EmailTokenService(
    @Value("\${loca-love.email-tokens.expiration}")
    private val tokenExpirationTime: Duration,

    private val emailTokenRepository: EmailTokenRepository
) {

    @Transactional
    fun generateToken(user: User): EmailToken =
        emailTokenRepository.save(
            EmailToken(UUID.randomUUID(), user, LocalDateTime.now())
        )

    @Transactional
    fun validateToken(token: String): User {
        val tokenValue = uuidFromString(token) ?: throw InvalidEmailTokenException()
        val emailToken = emailTokenRepository.findByValue(tokenValue) ?: throw EmailTokenNotFoundException()
        val tokenLifetime = Duration.between(emailToken.creationTime, LocalDateTime.now())

        if (tokenLifetime >= tokenExpirationTime) {
            throw EmailTokenExpiredException()
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

internal class InvalidEmailTokenException: RuntimeException("Invalid email token")

internal class EmailTokenNotFoundException: RuntimeException("Specified email token does not exist")

internal class EmailTokenExpiredException: RuntimeException("Invalid token")
