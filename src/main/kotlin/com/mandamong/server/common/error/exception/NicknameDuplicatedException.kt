package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode

class NicknameDuplicatedException(
    nickname: String,
) : DuplicatedKeyException(ErrorCode.DUPLICATED, "${ErrorCode.DUPLICATED}: $nickname")
