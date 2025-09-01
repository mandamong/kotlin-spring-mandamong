package com.mandamong.server.common.error.exception

import com.mandamong.server.common.error.ErrorCode

class TimeoutException : BadRequestException(ErrorCode.BAD_REQUEST, "제한 시간 초과")
