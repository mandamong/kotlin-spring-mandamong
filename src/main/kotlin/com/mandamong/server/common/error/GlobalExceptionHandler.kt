package com.mandamong.server.common.error

import com.mandamong.server.common.error.exception.BusinessBaseException
import com.mandamong.server.common.response.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(BusinessBaseException::class)
    fun handle(e: BusinessBaseException): ResponseEntity<ApiResponse<Nothing>> {
        log.error("BusinessBaseException", e)
        return ApiResponse.error(e.errorCode)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handle(e: HttpRequestMethodNotSupportedException): ResponseEntity<ApiResponse<Nothing>> {
        log.error("HttpRequestMethodNotSupportedException", e)
        return ApiResponse.error(ErrorCode.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ResponseEntity<ApiResponse<Nothing>> {
        log.error("Exception", e)
        return ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR)
    }
}
