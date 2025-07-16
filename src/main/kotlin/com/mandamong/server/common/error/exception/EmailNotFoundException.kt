package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode

class EmailNotFoundException(
    val email: String,
) : NotFoundException(ErrorCode.NOT_FOUND_EMAIL, "${ErrorCode.NOT_FOUND_EMAIL.message}: $email")
