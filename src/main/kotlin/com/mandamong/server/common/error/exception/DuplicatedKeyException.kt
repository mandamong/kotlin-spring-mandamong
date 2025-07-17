package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode

open class DuplicatedKeyException(
    errorCode: ErrorCode = ErrorCode.DUPLICATED,
    message: String = errorCode.message,
) : BusinessBaseException(errorCode, message)
