package com.localove.api.edit

data class PasswordDto(
    val password: String
)

data class NewPasswordDto(
    val token: String,
    val password: String
)