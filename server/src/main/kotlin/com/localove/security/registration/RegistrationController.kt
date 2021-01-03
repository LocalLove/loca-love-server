package com.localove.security.registration

import com.localove.api.UserRegistrationDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RegistrationController(
    private val registrationService: RegistrationService
) {
    @PostMapping("/sign-up")
    fun signUp(@RequestBody dto: UserRegistrationDto) {
        registrationService.signUp(dto)
    }
}