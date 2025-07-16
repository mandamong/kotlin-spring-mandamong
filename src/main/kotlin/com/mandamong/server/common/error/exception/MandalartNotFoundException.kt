package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode

class MandalartNotFoundException(
    mandalartId: Long,
) : NotFoundException(ErrorCode.NOT_FOUND_MANDALART, "${ErrorCode.NOT_FOUND_MANDALART}: $mandalartId")
