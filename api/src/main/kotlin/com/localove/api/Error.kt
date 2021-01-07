package com.localove.api

enum class ErrorType {
    VALIDATION_ERROR,
    WRONG_CREDENTIALS,
    INVALID_TOKEN,
    NOT_FOUND,
    ACCESS_DENIED,
    LOGIN_EXIST,
    EMAIL_EXIST,
    EMAIL_NOT_EXIST,
    WRONG_PASSWORD,
    INVALID_USER,
    LAST_PICTURE_DELETION,
    UNCONFIRMED,
    FIRST_START_CONFIG_REQUIRED,
}

data class ErrorDto(
    val error: ErrorType,
    val message: String? = null
)
