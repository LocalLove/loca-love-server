package com.localove.security.login

import com.localove.api.Credentials
import com.localove.api.TokenDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    private val service: LoginService
) {
    @PostMapping("/sign-in")
    fun signIn(@RequestBody credentials: Credentials): TokenDto {
        return service.signIn(credentials)
    }
}