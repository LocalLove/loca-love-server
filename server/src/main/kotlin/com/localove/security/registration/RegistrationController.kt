package com.localove.security.registration

import com.localove.api.security.UserRegistrationDto
import com.localove.security.EmailConfig
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RegistrationController(
    private val registrationService: RegistrationService,
    private val config: EmailConfig
) {
    @PostMapping("/sign-up")
    fun signUp(@RequestBody dto: UserRegistrationDto) {
        registrationService.signUp(dto)
    }
}