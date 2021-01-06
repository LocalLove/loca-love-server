package com.localove.util

import io.konform.validation.Invalid
import io.konform.validation.Valid
import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern

fun <T> Validation<T>.isValid(o: T): Boolean =
    when(val res = this.validate(o)){
        is Valid -> true
        is Invalid -> false
    }

class Validations {

    companion object {
        val passwordValidation = Validation<String> {
            pattern("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@_.]{8,100}\$")
        }
    }
}