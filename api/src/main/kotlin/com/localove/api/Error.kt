package com.localove.api

enum class ErrorType {
    VALIDATION_ERROR,
    WRONG_CREDENTIALS,
    INVALID_TOKEN,
    NOT_FOUND,
    ACCESS_DENIED,
    LOGIN_EXIST,
    EMAIL_EXIST,
    WRONG_PASSWORD,
    UNCONFIRMED,
    FIRST_START_CONFIG_REQUIRED,
    INVALID_USER
}

data class ErrorDto(
    val error: ErrorType,
    val message: String? = null
)