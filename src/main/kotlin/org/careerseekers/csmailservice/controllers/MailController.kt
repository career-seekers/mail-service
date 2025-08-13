package org.careerseekers.csmailservice.controllers

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mail-service/v1/mail")
class MailController(private val mailer: JavaMailSender) {

    @Value("\${spring.mail.username}")
    private val senderEmail: String? = null

    @PostMapping("/testSend")
    fun testSend() {
        val message = SimpleMailMessage()
        message.from = senderEmail
        message.setTo("scobca18@yandex.ru")
        message.subject = "test sub"
        message.text = "test"
        mailer.send(message)
    }
}