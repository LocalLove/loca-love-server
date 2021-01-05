package com.localove.api.edit

import java.time.LocalDate

data class BaseProfileEditDto(
    val login: String?,
    val name: String?,
    val dateOfBirth: LocalDate?,
    val status: String?,
    val bio: String?,
)