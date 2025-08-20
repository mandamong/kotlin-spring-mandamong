package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode

class IdNotFoundException(
    id: Long,
) : BadRequestException(ErrorCode.NOT_FOUND, "${ErrorCode.NOT_FOUND.message}: $id")
