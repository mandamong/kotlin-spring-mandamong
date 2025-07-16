package com.mandamong.server.common.error

import com.mandamong.server.common.error.exception.BusinessBaseException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handle(e: HttpRequestMethodNotSupportedException): ResponseEntity<ErrorResponse> {
        log.error("HttpRequestMethodNotSupportedException", e)
        return createErrorResponse(ErrorCode.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(BusinessBaseException::class)
    fun handle(e: BusinessBaseException): ResponseEntity<ErrorResponse> {
        log.error("BusinessBaseException", e)
        return createErrorResponse(e.errorCode)
    }

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ResponseEntity<ErrorResponse> {
        log.error("Exception", e)
        return createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR)
    }

    private fun createErrorResponse(errorCode: ErrorCode): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(errorCode.status).body(ErrorResponse.of(errorCode))
    }

}
