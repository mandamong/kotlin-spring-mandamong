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
        /**
             * 빈 성공 응답(HTTP 200)을 반환합니다.
             *
             * 성공 플래그(success)가 true이고 payload와 error가 모두 null인 ApiResponse를 HTTP 200 응답으로 래핑하여 반환합니다.
             *
             * @return HTTP 200 상태의 ResponseEntity<ApiResponse<R>>
             */
            fun <R> ok(): ResponseEntity<ApiResponse<R>> =
            ResponseEntity.ok(ApiResponse(success = true, payload = null, error = null))

        /**
             * HTTP 200(OK) 응답으로 성공 ApiResponse를 payload와 함께 반환합니다.
             *
             * ApiResponse의 `success`는 true, `payload`는 전달된 `data`, `error`는 null로 설정됩니다.
             *
             * @param data 응답 바디에 담길 페이로드
             * @return HTTP 상태 200과 ApiResponse를 포함한 ResponseEntity
             */
            fun <R> ok(data: R): ResponseEntity<ApiResponse<R>> =
            ResponseEntity.ok(ApiResponse(success = true, payload = data, error = null))

        /**
                 * 주어진 데이터를 담아 HTTP 201 Created 응답을 생성합니다.
                 *
                 * @param data 응답 본문의 payload로 포함될 값
                 * @return HTTP 상태 201 및 ApiResponse(success = true, payload = `data`, error = null)를 가진 ResponseEntity
                 */
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
