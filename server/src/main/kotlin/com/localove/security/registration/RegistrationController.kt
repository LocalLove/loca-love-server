package com.localove.security.registration

import com.localove.api.security.UserRegistrationDto
import com.localove.exceptions.AlreadyExistsException
import com.localove.security.entities.User
import com.localove.util.Response
import com.localove.util.Validations
import com.localove.util.throwIfNotValid
import io.konform.validation.Validation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RegistrationController(
    private val registrationService: RegistrationService
) {
    private val validation = Validation<UserRegistrationDto> {
        UserRegistrationDto::login {
            run(Validations.loginValidation)
        }

        UserRegistrationDto::email {
            run(Validations.emailValidation)
        }

        UserRegistrationDto::password {
            run(Validations.passwordValidation)
        }

        UserRegistrationDto::name {
            run(Validations.nameValidation)
        }
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody dto: UserRegistrationDto): ResponseEntity<*> {
        validation.throwIfNotValid(dto)

        return try {
            registrationService.signUp(dto.toUser())
            Response.ok()
        } catch (exc: AlreadyExistsException) {
            Response.error(exc.getErrorType(), exc.message)
        }
    }
}

private fun UserRegistrationDto.toUser() = User(
    name = name,
    login = login,
    email = email,
    password = password
)