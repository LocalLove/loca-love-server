package com.localove.api

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

enum class Gender {
    MALE,
    FEMALE
}

data class UserRegistrationDto(
    val login: String,
    val email: String,
    val password: String,
    val name: String,
    @JsonFormat(pattern = "dd/MM/yyyy")
    val dateOfBirth: LocalDate,
    val gender: Gender
)