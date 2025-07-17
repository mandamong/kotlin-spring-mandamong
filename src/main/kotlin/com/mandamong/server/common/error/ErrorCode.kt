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
    DUPLICATED("common.duplicated", "중복 오류", HttpStatus.CONFLICT),

    UNAUTHORIZED("common.unauthorized", "인증 오류", HttpStatus.UNAUTHORIZED),

    EXPIRED_TOKEN("auth.token.expired", "토큰 만료 오류", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("auth.token.invalid", "유효하지 않은 토큰", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_TOKEN("auth.token.unsupported", "지원하지 않는 토큰", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND("auth.token.not-found", "토큰이 존재하지 않음", HttpStatus.BAD_REQUEST),
    ;

}
