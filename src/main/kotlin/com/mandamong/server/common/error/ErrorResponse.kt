package com.mandamong.server.common.error

class ErrorResponse(
    val code: String,
    val message: String? = null,
) {

    companion object {
        fun of(errorCode: ErrorCode): ErrorResponse {
            return ErrorResponse(code = errorCode.code, message = errorCode.message)
        }

        fun of(errorCode: ErrorCode, message: String): ErrorResponse {
            return ErrorResponse(code = errorCode.code, message = message)
        }
    }

}
