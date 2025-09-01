package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode
import com.mandamong.server.common.error.exception.base.BusinessBaseException

open class BadRequestException(
    errorCode: ErrorCode = ErrorCode.BAD_REQUEST,
    message: String = errorCode.message,
) : BusinessBaseException(errorCode, message)
