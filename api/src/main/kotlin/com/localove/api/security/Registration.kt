package com.localove.api.security

import com.localove.api.user.Gender
import java.time.LocalDate

data class UserRegistrationDto(
    val login: String,
    val email: String,
    val password: String,
    val name: String,
    val birthDate: LocalDate,
    val gender: Gender
)