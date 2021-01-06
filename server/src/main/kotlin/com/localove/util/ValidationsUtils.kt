package com.localove.util

import com.localove.exceptions.ValidationException
import io.konform.validation.*

fun <T> Validation<T>.throwIfNotValid(o: T) =
    when(val res = validate(o)){
        is Valid -> {}
        is Invalid -> throw ValidationException(res.errors[0].message)
    }

class Validations {
    companion object {
        val loginValidation = Validation<String> {
            matches(Regex("[A-Za-z\\d_.]{3,100}\$"), "Invalid login")
        }

        val emailValidation = Validation<String> {
            matches(Regex("(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"), "Invalid email")
        }

        val passwordValidation = Validation<String> {
            matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@_.]{8,100}\$"), "Invalid password")
        }

        val nameValidation = Validation<String> {
            matches(Regex("[A-Za-z]{1,100}\$"), "Invalid name")
        }
    }
}

fun ValidationBuilder<String>.matches(regex: Regex, errorMessage: String): Constraint<String> {
    return addConstraint(
        errorMessage
    ) { regex.matches(it) }
}