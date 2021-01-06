package com.localove.security.registration

import com.localove.api.security.UserRegistrationDto
import com.localove.security.RoleManagementService
import com.localove.security.TokenService
import com.localove.security.UserService
import com.localove.security.entities.EmailTokenRepository
import com.localove.security.entities.Role
import com.localove.security.entities.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class RegistrationService(
    private val userService: UserService,
    private val tokenService: TokenService,
    private val roleManagementService: RoleManagementService,
    private val emailTokenRepository: EmailTokenRepository
) {
    @Transactional
    fun signUp(user: UserRegistrationDto) {
        // save user to db
        userService.saveUser(user.toUser())
        // email logic here
        // ...
    }

    @Transactional
    fun confirmEmail(token: String) {
        val user = tokenService.validateToken(emailTokenRepository, token)
        roleManagementService.removeRoleFromUser(user.id!!, Role.Name.UNCONFIRMED)
        roleManagementService.addRoleToUser(user.id!!, Role.Name.NEWCOMER)
    }
}

// mda
private fun UserRegistrationDto.toUser() = User(
    name = name,
    login = login,
    email = email,
    password = password
)