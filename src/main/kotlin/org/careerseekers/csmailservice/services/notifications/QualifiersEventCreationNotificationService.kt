package org.careerseekers.csmailservice.services.notifications

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.EventCreationDto
import org.careerseekers.csmailservice.enums.EventTypes
import org.careerseekers.csmailservice.services.interfaces.IEventCreationProcessingService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class QualifiersEventCreationNotificationService(
    @param:Qualifier("productionMailSender") val mailer: JavaMailSender,
    private val mailProperties: MailProperties,
    private val templateEngine: TemplateEngine
) : IEventCreationProcessingService {
    override val eventType = EventTypes.MASTER_CLASS

    override fun handle(record: ConsumerRecord<String, EventCreationDto>) {
        val kafkaMessage = record.value()

        val context = Context()
        context.setVariable("competenceName", kafkaMessage.directionName)
        context.setVariable("ageCategory", kafkaMessage.ageCategory)
        context.setVariable("expertName", kafkaMessage.expertName)
        context.setVariable("expertEmail", kafkaMessage.expertEmail)

        val htmlContent = templateEngine.process("master-class", context)

        val message = mailer.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")
        helper.setTo("scobca18@yandex.ru")
        helper.setFrom(mailProperties.productionMail.username)
        helper.setSubject("Добавлено нвоое событие в компетенции | Чемпионат Искатели Профессий")
        helper.setText(htmlContent, true)

        val resource = ClassPathResource("static/images/logo.png")
        helper.addInline("logoImage", resource)

        mailer.send(message)
    }
}