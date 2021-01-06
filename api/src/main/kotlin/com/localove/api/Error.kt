package com.localove.api

enum class ErrorType {
    VALIDATION_ERROR,
    WRONG_CREDENTIALS,
    WRONG_TOKEN,
    NOT_FOUND,
    ACCESS_DENIED,
    LOGIN_EXIST,
    EMAIL_EXIST,
    WRONG_PASSWORD
}

data class ErrorDto(
    val error: ErrorType,
    val message: String? = null
)