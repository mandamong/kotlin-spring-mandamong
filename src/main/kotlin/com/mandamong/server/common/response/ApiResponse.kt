package com.mandamong.server.common.response

import com.mandamong.server.common.error.ErrorResponse
import com.mandamong.server.common.error.exception.BusinessBaseException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: ErrorResponse?,
) {

    companion object {
        fun <R> ok(): ResponseEntity<ApiResponse<R>> {
            return ResponseEntity.ok(ApiResponse(success = true, data = null, error = null))
        }

        fun <R> ok(data: R): ResponseEntity<ApiResponse<R>> {
            return ResponseEntity.ok(ApiResponse(success = true, data = data, error = null))
        }

        fun <R> created(data: R): ResponseEntity<ApiResponse<R>> {
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse(success = true, data = data, error = null))
        }

        fun deleted(): ResponseEntity<ApiResponse<Nothing>> {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse(success = true, data = null, error = null))
        }

        fun <R> error(e: BusinessBaseException): ResponseEntity<ApiResponse<R>> {
            return ResponseEntity.status(e.errorCode.status)
                .body(ApiResponse(success = false, data = null, error = ErrorResponse.of(e.errorCode)))
        }
    }

}
