package com.mandamong.server.common.error

import com.mandamong.server.common.dto.ApiResponse
import com.mandamong.server.common.error.exception.base.BusinessBaseException
import com.mandamong.server.common.notification.discord.DiscordWebhookService
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.util.ContentCachingRequestWrapper

@RestControllerAdvice
class GlobalExceptionHandler(
    private val discordWebhookService: DiscordWebhookService,
) {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(BusinessBaseException::class)
    fun handle(e: BusinessBaseException): ResponseEntity<ApiResponse<Nothing>> {
        log.error("BusinessBaseException", e)
        return ApiResponse.error(e.errorCode)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handle(e: HttpRequestMethodNotSupportedException): ResponseEntity<ApiResponse<Nothing>> {
        log.error("HttpRequestMethodNotSupportedException", e)
        return ApiResponse.error(ErrorCode.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception, request: HttpServletRequest): ResponseEntity<ApiResponse<Nothing>> {
        log.error("Exception", e)

        // Discord 알림 전송 (비동기로 처리하여 응답 지연 방지)
        try {
            val requestInfo = buildRequestInfo(request)
            discordWebhookService.sendErrorNotification(e, requestInfo)
        } catch (notificationException: Exception) {
            log.error("Failed to send Discord notification", notificationException)
        }

        return ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR)
    }

    private fun buildRequestInfo(request: HttpServletRequest): String {
        return buildString {
            append("**Method:** ${request.method}\n")
            append("**URL:** ${request.requestURL}\n")
            request.queryString?.let { append("**Query:** $it\n") }
            append("**Content-Type:** ${request.contentType ?: "N/A"}\n")
            append("**Remote Address:** ${request.remoteAddr}\n")
            request.getHeader("User-Agent")?.let { append("**User-Agent:** $it") }
        }
    }

    private fun getRequestBody(request: HttpServletRequest): String? {
        return try {
            val method = request.method
            log.info("Getting request body for ${method} request. Request type: ${request.javaClass.simpleName}")
            
            if (method in listOf("POST", "PUT", "PATCH")) {
                when (request) {
                    is ContentCachingRequestWrapper -> {
                        // 캐시된 JSON body 사용
                        val contentAsByteArray = request.contentAsByteArray
                        val cachedBody = if (contentAsByteArray.isNotEmpty()) {
                            String(contentAsByteArray, Charsets.UTF_8)
                        } else ""
                        
                        log.info("Found ContentCachingRequestWrapper with body length: ${cachedBody.length}")
                        cachedBody.ifBlank {
                            log.info("Cached body is blank, falling back to parameters")
                            getRequestParameters(request)
                        }
                    }
                    else -> {
                        // 일반적인 경우 파라미터 정보 수집
                        log.info("Request is not ContentCachingRequestWrapper, using parameters")
                        getRequestParameters(request)
                    }
                }
            } else null
        } catch (e: Exception) {
            log.warn("Failed to read request body: ${e.message}")
            null
        }
    }
    
    private fun getRequestParameters(request: HttpServletRequest): String? {
        val params = request.parameterMap
        return if (params.isNotEmpty()) {
            buildString {
                append("Request Parameters:\n")
                params.entries.forEach { (key, values) ->
                    append("$key = ${values.joinToString(", ")}\n")
                }
            }.trim()
        } else {
            val contentType = request.contentType
            if (contentType != null) {
                "Content-Type: $contentType"
            } else null
        }
    }
}
