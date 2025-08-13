package org.careerseekers.csmailservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class MailConfig {
    @Value("\${spring.mail.host}")
    private val host: String? = null

    @Value("\${spring.mail.username}")
    private val username: String? = null

    @Value("\${spring.mail.password}")
    private val password: String? = null

    @Value("\${spring.mail.port}")
    private val port: Int = 0

    @Value("\${spring.mail.protocol}")
    private val protocol: String? = null

    @Value("\${spring.mail.debug}")
    private val debug: String = "false"

    @Bean
    fun mailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port
        mailSender.username = username
        mailSender.password = password

        val props = mailSender.javaMailProperties
        props.setProperty("mail.transport.protocol", protocol)
        props.setProperty("mail.debug", debug)

        return mailSender
    }
}