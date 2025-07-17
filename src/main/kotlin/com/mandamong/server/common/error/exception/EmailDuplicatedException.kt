package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode

class EmailDuplicatedException(
    email: String,
) : DuplicatedKeyException(ErrorCode.DUPLICATED, "${ErrorCode.DUPLICATED}: $email")
