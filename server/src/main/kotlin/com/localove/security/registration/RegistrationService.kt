package com.localove.security.registration

import com.localove.api.security.UserRegistrationDto
import com.localove.security.UserService
import com.localove.security.entities.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegistrationService(
    private val userService: UserService
) {
    @Transactional
    fun signUp(user: UserRegistrationDto) {
        // save user to db
        userService.saveUser(user.toUser())
        // email logic here
        // ...
    }
}

// mda
private fun UserRegistrationDto.toUser() = User(
    name = name,
    login = login,
    email = email,
    password = password
)