package com.localove.exceptions

import com.localove.api.ErrorType

class AlreadyExistsException(val property: Property) : RuntimeException(
    "User with such $property already exists"
) {
    enum class Property {
        LOGIN,
        EMAIL
    }

    fun getErrorType(): ErrorType = if (property == Property.LOGIN) {
        ErrorType.LOGIN_EXIST
    } else {
        ErrorType.EMAIL_EXIST
    }
}