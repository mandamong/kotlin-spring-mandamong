package com.mandamong.server.common.util.cookie

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class CookieUtil {

    fun add(response: HttpServletResponse, key: String, value: String, maxAge: Int) {
        val cookie = Cookie(key, value).apply {
            path = "/"
            isHttpOnly = false
            this.maxAge = maxAge
            secure = true
            domain = "mandamong.sailin.cloud"
        }
        response.addCookie(cookie)
    }

    fun delete(request: HttpServletRequest, response: HttpServletResponse, key: String) {
        val cookie = request.cookies?.find { it.name == key }
        if (cookie != null) {
            val deleteCookie = Cookie(cookie.name, "").apply {
                path = cookie.path
                maxAge = 0
                isHttpOnly = cookie.isHttpOnly
                secure = cookie.secure
                domain = cookie.domain
            }
            response.addCookie(deleteCookie)
        }
    }

}
