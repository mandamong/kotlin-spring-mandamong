package com.mandamong.server.common.util.json

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class JsonUtil(
    private val objectMapper: ObjectMapper,
) {

    fun <T> convert(value: Any, clazz: Class<T>): T {
        return objectMapper.convertValue(value, clazz)
    }

}
