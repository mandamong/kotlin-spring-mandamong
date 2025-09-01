package com.mandamong.server.common.error

data class ErrorResponse(
    val code: String,
    val message: String? = null,
) {
    companion object {
        fun of(errorCode: ErrorCode): ErrorResponse = ErrorResponse(code = errorCode.code, message = errorCode.message)

        fun of(
            errorCode: ErrorCode,
            message: String,
        ): ErrorResponse = ErrorResponse(code = errorCode.code, message = message)
    }
}
