package com.localove.util

import com.localove.api.ErrorDto
import com.localove.api.ErrorType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object Response {
    fun ok(): ResponseEntity<Unit> = ResponseEntity.ok().build()

    fun <T> ok(body: T): ResponseEntity<T> = ResponseEntity.ok().body(body)

    fun error(
        errorType: ErrorType,
        message: String? = null,
    ): ResponseEntity<ErrorDto> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorDto(errorType, message))
}



