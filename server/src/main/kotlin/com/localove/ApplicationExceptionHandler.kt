package com.localove

import com.localove.api.ErrorDto
import com.localove.api.ErrorType
import com.localove.exceptions.NotFoundException
import com.localove.exceptions.ValidationException
import com.localove.util.LoggerProperty
import com.localove.util.Response
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ApplicationExceptionHandler {
    private val logger by LoggerProperty()

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(exc: NotFoundException): ResponseEntity<ErrorDto> {
        logger.error("NoDataFoundException: '${exc.localizedMessage}'")
        return Response.error(ErrorType.VALIDATION_ERROR, exc.localizedMessage)
    }

    @ExceptionHandler(HttpMessageConversionException::class)
    fun handleConversionError(exc: HttpMessageConversionException): ResponseEntity<ErrorDto> {
        logger.error(defaultErrorMessage(exc))
        return Response.error(ErrorType.VALIDATION_ERROR, "Wrong data")
    }

    // TODO
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleConversionError(exc: IllegalArgumentException): ResponseEntity<ErrorDto> {
        logger.error(defaultErrorMessage(exc))
        return Response.error(ErrorType.VALIDATION_ERROR, exc.localizedMessage)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleRequestParameterError(exc: MissingServletRequestParameterException): ResponseEntity<ErrorDto> {
        logger.error(defaultErrorMessage(exc))
        return Response.error(ErrorType.VALIDATION_ERROR, exc.localizedMessage)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(exc: ValidationException): ResponseEntity<ErrorDto> {
        logger.error(defaultErrorMessage(exc))
        return Response.error(ErrorType.VALIDATION_ERROR, exc.localizedMessage)
    }

    private fun defaultErrorMessage(exc: Exception): String = "${exc::class.simpleName}: '${exc.localizedMessage}'"
}