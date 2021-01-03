package com.localove.exceptions

class AlreadyExistsException(val property: Property) : RuntimeException(
    "User with such $property already exists"
) {
    enum class Property {
        LOGIN,
        EMAIL
    }
}