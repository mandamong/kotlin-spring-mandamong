package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode

class EmailNotFoundException(
    email: String,
) : BadRequestException(ErrorCode.NOT_FOUND, "${ErrorCode.NOT_FOUND.message}: $email")
