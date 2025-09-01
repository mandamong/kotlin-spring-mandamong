package com.mandamong.server.common.dto

import com.mandamong.server.common.error.ErrorCode
import com.mandamong.server.common.error.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ApiResponse<T>(
    val success: Boolean,
    val payload: T?,
    val error: ErrorResponse?,
) {
    companion object {
        fun <R> ok(): ResponseEntity<ApiResponse<R>> = ResponseEntity.ok(ApiResponse(success = true, payload = null, error = null))

        fun <R> ok(data: R): ResponseEntity<ApiResponse<R>> = ResponseEntity.ok(ApiResponse(success = true, payload = data, error = null))

        fun <R> created(data: R): ResponseEntity<ApiResponse<R>> =
            ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse(success = true, payload = data, error = null))

        fun deleted(): ResponseEntity<ApiResponse<Nothing>> = ResponseEntity.status(HttpStatus.NO_CONTENT).build()

        fun error(errorCode: ErrorCode): ResponseEntity<ApiResponse<Nothing>> =
            ResponseEntity
                .status(errorCode.status)
                .body(ApiResponse(success = false, payload = null, error = ErrorResponse.of(errorCode)))
    }
}
