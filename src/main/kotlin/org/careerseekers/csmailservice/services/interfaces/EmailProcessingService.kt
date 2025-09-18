package org.careerseekers.csmailservice.services.interfaces

import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.enums.MailEventTypes
import org.springframework.mail.javamail.JavaMailSender

interface EmailProcessingService : KafkaMessageHandler<String, EmailSendingTaskDto> {
    val mailer: JavaMailSender
    val eventType: MailEventTypes
}