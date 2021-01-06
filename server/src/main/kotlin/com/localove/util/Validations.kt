package com.localove.util

import io.konform.validation.Invalid
import io.konform.validation.Valid
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern

fun <T> Validation<T>.isValid(o: T): Boolean =
    when(val res = this.validate(o)){
        is Valid -> true
        is Invalid -> false
    }

class Validations {

    companion object {
        val passwordValidation = Validation<String> {
            minLength(8)
            maxLength(100)
            pattern("^(?=.*?[A-Za-z])(?=.*?[0-9])(?=*?[!@_.])\$")
        }
    }
}