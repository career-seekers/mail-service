package org.careerseekers.csmailservice.services.notifications

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.EventCreationDto
import org.careerseekers.csmailservice.enums.EventTypes
import org.careerseekers.csmailservice.services.interfaces.IEventCreationProcessingService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine

@Service
class QualifiersEventCreationNotificationService(
    @param:Qualifier("productionMailSender") val mailer: JavaMailSender,
    private val mailProperties: MailProperties,
    private val templateEngine: TemplateEngine
) : IEventCreationProcessingService {
    override val eventType = EventTypes.QUALIFIERS

    override fun handle(record: ConsumerRecord<String, EventCreationDto>) {
        val kafkaMessage = record.value()
        val message = createEventNotification(
            kafkaMessage = kafkaMessage,
            templateName = "qualifiers",
            templateEngine = templateEngine,
            mailer = mailer,
            fromEmail = mailProperties.productionMail.username,
            sentTo = emptyArray(),
        )

        mailer.send(message)
    }
}