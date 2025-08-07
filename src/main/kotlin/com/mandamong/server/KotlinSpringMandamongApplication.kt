package com.mandamong.server

import com.mandamong.server.common.order.AopOrder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableCaching(order = AopOrder.CACHING)
@EnableScheduling
class KotlinSpringMandamongApplication

fun main(args: Array<String>) {
    runApplication<KotlinSpringMandamongApplication>(*args)
}
