package com.mandamong.server.common.test

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController {

    @GetMapping("/error")
    fun triggerError(): String {
        throw RuntimeException("Test error for Discord notification")
    }

    @GetMapping("/null-pointer")
    fun triggerNullPointer(): String {
        val nullString: String? = null
        return nullString!!.length.toString() // 의도적으로 NPE 발생
    }

    @GetMapping("/arithmetic")
    fun triggerArithmeticError(): String {
        @Suppress("DIVISION_BY_ZERO")
        val result = 10 / 0
        return result.toString()
    }
}
