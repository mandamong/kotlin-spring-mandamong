package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode

open class BadRequestException(
    errorCode: ErrorCode = ErrorCode.BAD_REQUEST_ERROR,
    message: String = errorCode.message
) : BusinessBaseException(errorCode, message)

