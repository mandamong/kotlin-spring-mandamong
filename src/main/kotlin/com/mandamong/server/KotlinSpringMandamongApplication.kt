package com.mandamong.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class KotlinSpringMandamongApplication

fun main(args: Array<String>) {
    runApplication<KotlinSpringMandamongApplication>(*args)
}
