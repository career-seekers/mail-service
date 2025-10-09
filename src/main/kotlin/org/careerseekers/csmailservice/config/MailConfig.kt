package org.careerseekers.csmailservice.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class MailConfig(private val mailProperties: MailProperties) {

    @Bean
    @Qualifier("productionMailSender")
    fun productionMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = mailProperties.host
        mailSender.port = mailProperties.port.toInt()
        mailSender.username = mailProperties.productionMail.username
        mailSender.password = mailProperties.productionMail.password

        val props = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.ssl.enable"] = "false"
        props["mail.debug"] = "true"

        return mailSender
    }
}