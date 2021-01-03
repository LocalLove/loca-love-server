package com.localove.api

import java.util.*

enum class Gender {
    MALE,
    FEMALE
}

data class UserRegistrationDto(
    val login: String,
    val email: String,
    val password: String,
    val name: String,
    val dateOfBirth: Date,
    val gender: Gender
)