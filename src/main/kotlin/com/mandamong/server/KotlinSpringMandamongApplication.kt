package com.mandamong.server

import com.mandamong.server.common.order.AopOrder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableCaching(order = AopOrder.CACHING)
class KotlinSpringMandamongApplication

fun main(args: Array<String>) {
    runApplication<KotlinSpringMandamongApplication>(*args)
}
