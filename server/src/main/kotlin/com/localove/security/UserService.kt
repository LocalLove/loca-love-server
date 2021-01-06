package com.localove.security

import com.localove.exceptions.AlreadyExistsException
import com.localove.exceptions.NotFoundException
import com.localove.security.entities.RoleRepository
import com.localove.security.entities.User
import com.localove.security.entities.UserRepository
import com.localove.security.jwt.JwtAuthentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val roleRepository: RoleRepository
) {
    fun findById(id: Long): User {
        return userRepository
            .findById(id)
            .orElseThrow {
                NotFoundException("Wrong user id: $id")
            }
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
            // TODO: расскоментить когда добавим логику почты
//            val unconfirmedRole = roleRepository
//                .findByName(Role.Name.UNCONFIRMED)
//                .orElseThrow {
//                    IllegalArgumentException("DB doesn't contain predefined role: UNCONFIRMED")
//                }
//            roles.add(unconfirmedRole)
            userRepository.save(this)
        }
    }

    fun checkPassword(oldPassword: String): JwtAuthentication {
        return if (passwordEncoder.matches(oldPassword, ))
    }

    @Transactional
    fun editPassword()
}