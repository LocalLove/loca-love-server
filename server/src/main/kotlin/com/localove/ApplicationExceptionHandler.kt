package com.localove

import com.localove.api.ErrorDto
import com.localove.api.ErrorType
import com.localove.exceptions.NotFoundException
import com.localove.util.LoggerProperty
import org.springframework.http.HttpStatus
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
        return ResponseEntity(ErrorDto(ErrorType.NOT_FOUND, exc.localizedMessage), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(HttpMessageConversionException::class)
    fun handleConversionError(exc: HttpMessageConversionException): ResponseEntity<ErrorDto> {
        logger.error("HttpMessageConversionException: '${exc.localizedMessage}'")
        return ResponseEntity(ErrorDto(ErrorType.VALIDATION_ERROR,"Wrong data"), HttpStatus.BAD_REQUEST)
    }

    // TODO
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleConversionError(exc: IllegalArgumentException): ResponseEntity<ErrorDto> {
        logger.error("IllegalArgumentException: '${exc.localizedMessage}'")
        return ResponseEntity(ErrorDto(ErrorType.VALIDATION_ERROR, exc.localizedMessage), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleRequestParameterError(exc: MissingServletRequestParameterException): ResponseEntity<ErrorDto> {
        logger.error("MissingServletRequestParameterException: '${exc.localizedMessage}'")
        return ResponseEntity(ErrorDto(ErrorType.VALIDATION_ERROR, exc.localizedMessage), HttpStatus.BAD_REQUEST)
    }
}