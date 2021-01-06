package com.localove.security.registration

import com.localove.api.ErrorType
import com.localove.api.security.UserRegistrationDto
import com.localove.exceptions.InvalidTokenException
import com.localove.util.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
internal class RegistrationController(
    private val registrationService: RegistrationService
) {
    @PostMapping("/sign-up")
    fun signUp(@RequestBody dto: UserRegistrationDto) {
        registrationService.signUp(dto)
    }

    @PostMapping("/confirm")
    fun confirmEmail(@RequestParam("token") token: String): ResponseEntity<*> {
        return try {
            registrationService.confirmEmail(token)
            Response.ok()
        } catch (exc: InvalidTokenException) {
            Response.error(errorType = ErrorType.INVALID_TOKEN, message = exc.localizedMessage)
        }
    }
}