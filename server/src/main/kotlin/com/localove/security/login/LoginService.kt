package com.localove.security.login

import com.localove.api.security.Credentials
import com.localove.api.security.TokenDto
import com.localove.exceptions.NotFoundException
import com.localove.security.jwt.JwtService
import com.localove.security.entities.Role
import com.localove.security.UserService
import com.localove.security.entities.User
import com.localove.util.LoggerProperty
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LoginService(
    private val userService: UserService,
    private val bCryptPasswordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    private val logger by LoggerProperty()

    @Transactional
    fun signIn(credentials: Credentials): TokenDto {
        val user = userService.findByLoginOrEmail(credentials.login)

        if (!isConfirmed(user)) {
            throw IllegalArgumentException("Unconfirmed")
        }

        if (!bCryptPasswordEncoder.matches(credentials.password, user.password)) {
            throw NotFoundException("User with such credentials wasn't found")
        }

        logger.info("User ${user.email} logged in")
        return TokenDto(
            jwtService.generateToken(user.id!!)
        )
    }

    private fun isConfirmed(user: User): Boolean {
        return !user.roles.any { it.name == Role.Name.UNCONFIRMED }
    }
}