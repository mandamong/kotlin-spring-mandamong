package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode

open class NotFoundException(
    errorCode: ErrorCode = ErrorCode.NOT_FOUND,
    message: String = errorCode.message,
) : BusinessBaseException(errorCode, message)
