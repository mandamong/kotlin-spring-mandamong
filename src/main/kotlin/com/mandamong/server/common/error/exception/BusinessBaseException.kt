package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode

open class BusinessBaseException(
    val errorCode: ErrorCode,
    message: String = errorCode.message
) : RuntimeException(message)
