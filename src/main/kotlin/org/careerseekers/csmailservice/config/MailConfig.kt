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
        props.setProperty("mail.transport.protocol", mailProperties.protocol)
        props.setProperty("mail.debug", mailProperties.debug)

        return mailSender
    }

    @Bean
    @Qualifier("serviceMailSender")
    fun serviceMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()

        mailSender.host = mailProperties.host
        mailSender.port = mailProperties.port.toInt()
        mailSender.username = mailProperties.serviceMail.username
        mailSender.password = mailProperties.serviceMail.password

        val props = mailSender.javaMailProperties
        props.setProperty("mail.transport.protocol", mailProperties.protocol)
        props.setProperty("mail.debug", mailProperties.debug)

        return mailSender
    }
}