package com.mandamong.api

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles(profiles = ["local"])
class KotlinSpringMandamongApplicationTests {
    @Test
    fun contextLoads() {
    }
}
