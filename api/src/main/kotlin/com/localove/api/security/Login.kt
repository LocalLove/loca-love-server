package com.localove.api.security

data class Credentials(
    val login: String,
    val password: String
)

data class TokenDto(
    val token: String
)