package org.careerseekers.csmailservice.services

import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.enums.MailEventTypes

interface EmailProcessingService {
    val eventType: MailEventTypes

    fun processEmail(message: EmailSendingTaskDto)
}