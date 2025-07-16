package com.mandamong.server.common.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: String,
    val message: String,
    val status: HttpStatus,
) {

    INTERNAL_SERVER_ERROR("internal.server.error", "서버 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR),
    METHOD_NOT_ALLOWED("common.method.not-allowed", "잘못된 HTTP 메서드 호출", HttpStatus.METHOD_NOT_ALLOWED),
    NOT_FOUND("common.not.found", "조회 오류", HttpStatus.NOT_FOUND),
    NOT_FOUND_EMAIL("user.email.not-found", "이메일 조회 오류", HttpStatus.BAD_REQUEST),
    ;

}
