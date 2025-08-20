package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode

class NicknameDuplicatedException(
    nickname: String,
) : BadRequestException(ErrorCode.DUPLICATED, "${ErrorCode.DUPLICATED}: $nickname")
