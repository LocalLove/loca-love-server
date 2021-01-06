package com.localove.profile

import com.localove.api.edit.PasswordDto
import com.localove.api.security.TokenDto
import com.localove.security.jwt.JwtAuthentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/user")
class ProfileController(
) {
    @PostMapping("/check-password")
    fun checkPassword(passwordDto: PasswordDto): TokenDto =
}

fun JwtAuthentication.toDto(): TokenDto = TokenDto(token)