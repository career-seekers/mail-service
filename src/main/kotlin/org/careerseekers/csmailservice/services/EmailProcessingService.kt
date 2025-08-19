package org.careerseekers.csmailservice.services

import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.enums.MailEventTypes
import org.springframework.mail.javamail.JavaMailSender

interface EmailProcessingService {
    val mailer: JavaMailSender
    val eventType: MailEventTypes

    fun processEmail(message: EmailSendingTaskDto)
}