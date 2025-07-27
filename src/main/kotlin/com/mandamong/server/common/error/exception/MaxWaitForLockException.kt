package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode

class MaxWaitForLockException(
    errorCode: ErrorCode = ErrorCode.MAX_WAIT_FOR_LOCK,
    message: String = errorCode.message,
) : BusinessBaseException(errorCode, message)
