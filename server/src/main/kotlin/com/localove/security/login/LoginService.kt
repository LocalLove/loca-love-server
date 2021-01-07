package com.localove.security.login

import com.localove.api.edit.BaseProfileEditDto
import com.localove.api.security.Credentials
import com.localove.api.security.TokenDto
import com.localove.exceptions.FirstStartConfigRequiredException
import com.localove.exceptions.NotFoundException
import com.localove.exceptions.UnconfirmedUserException
import com.localove.security.UserService
import com.localove.security.entities.Role
import com.localove.security.entities.User
import com.localove.security.jwt.JwtService
import com.localove.util.LoggerProperty
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LoginService(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val bCryptPasswordEncoder: PasswordEncoder
) {
    private val logger by LoggerProperty()

    @Transactional
    fun signIn(credentials: Credentials): TokenDto {
        val user = userService.findByLoginOrEmail(credentials.login)

        if (user.isUnconfirmed()) {
            throw UnconfirmedUserException("Unconfirmed")
        }

        if (!bCryptPasswordEncoder.matches(credentials.password, user.password)) {
            throw NotFoundException("User with such credentials wasn't found")
        }

        val jwt = jwtService.generateToken(user.id!!)
        return when {
            user.isNewcomer() -> throw FirstStartConfigRequiredException(jwt)
            else -> {
                logger.info("User ${user.email} logged in")
                TokenDto(jwt)
            }
        }
    }

    fun User.isUnconfirmed(): Boolean {
        return roles.any { it.name == Role.Name.UNCONFIRMED }
    }

    fun User.isNewcomer(): Boolean {
        return roles.any { it.name == Role.Name.NEWCOMER }
    }
}