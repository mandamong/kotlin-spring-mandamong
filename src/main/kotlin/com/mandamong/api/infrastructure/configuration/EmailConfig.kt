package com.mandamong.api.infrastructure.configuration

import java.util.Properties
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class EmailConfig(
    @Value("\${spring.mail.host}")
    private val host: String,

    @Value("\${spring.mail.port}")
    private val port: Int,

    @Value("\${spring.mail.username}")
    private val username: String,

    @Value("\${spring.mail.password}")
    private val password: String,

    @Value("\${spring.mail.properties.mail.smtp.auth}")
    private val auth: Boolean,

    @Value("\${spring.mail.properties.mail.smtp.starttls.enable}")
    private val starttlsEnable: Boolean,

    @Value("\${spring.mail.properties.mail.smtp.starttls.required}")
    private val starttlsRequired: Boolean,

    @Value("\${spring.mail.properties.mail.smtp.connection-timeout}")
    private val connectionTimeout: Int,

    @Value("\${spring.mail.properties.mail.smtp.timeout}")
    private val timeout: Int,

    @Value("\${spring.mail.properties.mail.smtp.write-timeout}")
    private val writeTimeout: Int,
) {

    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port
        mailSender.username = username
        mailSender.password = password
        mailSender.defaultEncoding = "UTF-8"
        mailSender.javaMailProperties = mailProperties
        return mailSender
    }

    val mailProperties: Properties
        get() {
            val properties: Properties = Properties()
            properties["mail.smtp.auth"] = auth
            properties["mail.smtp.starttls.enable"] = starttlsEnable
            properties["mail.smtp.starttls.required"] = starttlsRequired
            properties["mail.smtp.connectiontimeout"] = connectionTimeout
            properties["mail.smtp.timeout"] = timeout
            properties["mail.smtp.writetimeout"] = writeTimeout
            return properties
        }
}
