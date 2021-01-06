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
import org.springframework.web.multipart.MultipartFile

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

        when {
            user.isUnconfirmed() -> throw UnconfirmedUserException("Unconfirmed")
            user.isNewbie() -> throw FirstStartConfigRequiredException("Please pass the initial profile setup")
        }

        if (!bCryptPasswordEncoder.matches(credentials.password, user.password)) {
            throw NotFoundException("User with such credentials wasn't found")
        }

        logger.info("User ${user.email} logged in")
        return TokenDto(
            jwtService.generateToken(user.id!!)
        )
    }

    fun firstStartConfiguration(
        avatar: MultipartFile,
        userInfo: BaseProfileEditDto) {

    }

    fun User.isUnconfirmed(): Boolean {
        return roles.any { it.name == Role.Name.UNCONFIRMED }
    }

    fun User.isNewbie(): Boolean {
        return roles.any { it.name == Role.Name.NEWBIE }
    }
}