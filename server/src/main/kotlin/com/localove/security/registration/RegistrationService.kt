package com.localove.security.registration

import com.localove.security.TokenService
import com.localove.security.UserService
import com.localove.security.email.SecurityEmailService
import com.localove.security.entities.EmailToken
import com.localove.security.entities.EmailTokenRepository
import com.localove.security.entities.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegistrationService(
    private val userService: UserService,
    private val emailService: SecurityEmailService,
    private val tokenService: TokenService,
    private val emailTokenRepository: EmailTokenRepository
) {
    @Transactional
    fun signUp(user: User) {
        userService.saveUser(user)

        val token = tokenService.fillToken(emailTokenRepository, user, EmailToken())
        emailService.sendEmailConfirmation(user.email, token.value)
    }
}