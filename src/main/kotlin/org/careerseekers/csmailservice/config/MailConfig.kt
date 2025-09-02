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

    @Value("\${spring.mail.port}")
    private val port: Int = 0

    @Value("\${spring.mail.protocol}")
    private val protocol: String? = null

    @Value("\${spring.mail.debug}")
    private val debug: String = "false"

    @Value("\${spring.mail.production_mail.username}")
    private val productionUsername: String? = null

    @Value("\${spring.mail.production_mail.password}")
    private val productionPassword: String? = null

    @Bean
    fun productionMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port
        mailSender.username = productionUsername
        mailSender.password = productionPassword

        val props = mailSender.javaMailProperties
        props.setProperty("mail.transport.protocol", protocol)
        props.setProperty("mail.debug", debug)

        return mailSender
    }
}