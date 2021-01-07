package com.localove.security

import com.localove.exceptions.AlreadyExistsException
import com.localove.exceptions.InvalidTokenException
import com.localove.exceptions.NotFoundException
import com.localove.exceptions.WrongPasswordException
import com.localove.security.email.SecurityEmailService
import com.localove.security.entities.*
import com.localove.security.jwt.JwtService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(
    private val jwtService: JwtService,
    private val tokenService: TokenService,
    private val emailService: SecurityEmailService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val roleRepository: RoleRepository,
    private val emailChangeTokenRepository: EmailChangeTokenRepository
) {
    fun findById(id: Long): User {
        return userRepository
            .findById(id)
            .orElseThrow {
                NotFoundException("Wrong user id: $id")
            }
    }

    fun getCurrentUser(): User {
        return userRepository.findCurrentUser()
            ?: throw IllegalArgumentException("Not authorized")
    }

    fun findByLoginOrEmail(loginOrEmail: String): User {
        return userRepository.findByLoginOrEmail(loginOrEmail, loginOrEmail)
            .orElseThrow {
                NotFoundException("User with such credentials wasn't found")
            }
    }

    @Transactional
    fun saveUser(user: User) {
        if (userRepository.existsByEmail(user.email)) {
            throw AlreadyExistsException(AlreadyExistsException.Property.EMAIL)
        }

        if (userRepository.existsByLogin(user.login)) {
            throw AlreadyExistsException(AlreadyExistsException.Property.LOGIN)
        }

        user.apply {
            password = passwordEncoder.encode(password)
            addRole(this, Role.Name.UNCONFIRMED)
            userRepository.save(this)
        }
    }

    @Transactional
    fun firstStartConfiguration() {
        val currentUser = getCurrentUser()
        removeRole(currentUser, Role.Name.NEWCOMER)
        addRole(currentUser, Role.Name.USER)
    }

    @Transactional
    fun editEmail(newEmail: String) {
        val currentUser = getCurrentUser()
        if (userRepository.existsByEmail(newEmail)) {
            val token = EmailChangeToken(newEmail)
            tokenService.fillToken(emailChangeTokenRepository, currentUser, token)

            emailService.sendEmailConfirmation(newEmail, token.value)
        } else {
            throw AlreadyExistsException(AlreadyExistsException.Property.EMAIL)
        }
    }

    fun checkPassword(oldPassword: String): String {
        val currentUser = getCurrentUser()
        return if (passwordEncoder.matches(oldPassword, currentUser.password)) {
            jwtService.generateToken(currentUser.id!!)
        } else {
            throw WrongPasswordException()
        }
    }

    @Transactional
    fun editPassword(newPassword: String, token: String) {
        val currentUser = getCurrentUser()
        val userId = jwtService.getPayload(token).userId
        if (userId == currentUser.id) {
            currentUser.password = passwordEncoder.encode(newPassword)
        } else {
            throw InvalidTokenException()
        }
    }

    @Transactional
    fun confirmNewEmail(token: String) {
        tokenService.validateToken(emailChangeTokenRepository, token)
        val newEmail = emailChangeTokenRepository.findByValue(
            UUID.fromString(token)
        )!!.email
        getCurrentUser().email = newEmail
    }

    private fun findRoleByName(roleName: Role.Name) =
        roleRepository.findByName(roleName)
            ?: throw IllegalArgumentException("DB doesn't contain predefined role: UNCONFIRMED")

    private fun removeRole(user: User, roleName: Role.Name) {
        user.roles.removeIf { it.name == roleName }
    }

    private fun addRole(user: User, roleName: Role.Name) {
        user.roles.add(findRoleByName(roleName))
    }
}
