package com.mandamong.server.common.error

import com.mandamong.server.common.error.exception.base.BusinessBaseException
import com.mandamong.server.common.dto.ApiResponse
import com.mandamong.server.common.notification.discord.DiscordWebhookService
import com.mandamong.server.common.util.log.log
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler(
    private val discordWebhookService: DiscordWebhookService
) {

    @ExceptionHandler(BusinessBaseException::class)
    fun handle(e: BusinessBaseException): ResponseEntity<ApiResponse<Nothing>> {
        log().error("BusinessBaseException", e)
        return ApiResponse.error(e.errorCode)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handle(e: HttpRequestMethodNotSupportedException): ResponseEntity<ApiResponse<Nothing>> {
        log().error("HttpRequestMethodNotSupportedException", e)
        return ApiResponse.error(ErrorCode.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception, request: HttpServletRequest): ResponseEntity<ApiResponse<Nothing>> {
        log().error("Exception", e)
        
        // Discord 알림 전송 (비동기로 처리하여 응답 지연 방지)
        try {
            val requestInfo = buildRequestInfo(request)
            discordWebhookService.sendErrorNotification(e, requestInfo)
        } catch (notificationException: Exception) {
            log().error("Failed to send Discord notification", notificationException)
        }
        
        return ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR)
    }
    
    private fun buildRequestInfo(request: HttpServletRequest): String {
        return buildString {
            append("**Method:** ${request.method}\n")
            append("**URL:** ${request.requestURL}\n")
            request.queryString?.let { append("**Query:** $it\n") }
            append("**Remote Address:** ${request.remoteAddr}\n")
            request.getHeader("User-Agent")?.let { append("**User-Agent:** $it") }
        }
    }
}
