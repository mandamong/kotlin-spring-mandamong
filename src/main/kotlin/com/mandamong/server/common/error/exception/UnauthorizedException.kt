package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode
import com.mandamong.server.common.error.exception.base.BusinessBaseException

open class UnauthorizedException(
    errorCode: ErrorCode = ErrorCode.UNAUTHORIZED,
    message: String = errorCode.message,
) : BusinessBaseException(errorCode, message)

