package org.careerseekers.csmailservice.services.interfaces

import jakarta.mail.internet.MimeMessage
import org.careerseekers.csmailservice.dto.EventCreationDto
import org.careerseekers.csmailservice.enums.EventTypes
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

interface IEventCreationProcessingService : IKafkaMessageHandler<String, EventCreationDto> {
    val eventType: EventTypes

    fun createEventNotification(
        kafkaMessage: EventCreationDto,
        templateName: String,
        templateEngine: TemplateEngine,
        mailer: JavaMailSender,
        fromEmail: String,
        sentTo: Array<String>,
    ): MimeMessage {
        val context = Context()
        context.setVariable("competenceName", kafkaMessage.directionName)
        context.setVariable("ageCategory", kafkaMessage.ageCategory)
        context.setVariable("expertName", kafkaMessage.expertName)
        context.setVariable("expertEmail", kafkaMessage.expertEmail)

        val htmlContent = templateEngine.process(templateName, context)

        val message = mailer.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")
        helper.setBcc(sentTo)
        helper.setFrom(fromEmail)
        helper.setSubject("Добавлено нвоое событие в компетенции | Чемпионат Искатели Профессий")
        helper.setText(htmlContent, true)

        val resource = ClassPathResource("static/images/logo.png")
        helper.addInline("logoImage", resource)

        return message
    }
}