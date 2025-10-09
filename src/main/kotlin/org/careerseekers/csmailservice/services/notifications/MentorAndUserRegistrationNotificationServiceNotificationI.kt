package org.careerseekers.csmailservice.services.notifications

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.enums.MailEventTypes
import org.careerseekers.csmailservice.services.interfaces.IEmailNotificationProcessingService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class MentorAndUserRegistrationNotificationServiceNotificationI(
    @param:Qualifier("productionMailSender") override val mailer: JavaMailSender,
    private val mailProperties: MailProperties,
    private val templateEngine: TemplateEngine
) : IEmailNotificationProcessingService {

    override val eventType = MailEventTypes.MENTOR_AND_USER_REGISTRATION

    override fun handle(record: ConsumerRecord<String, EmailSendingTaskDto>) {
        val kafkaMessage = record.value()
        val user = kafkaMessage.user!!

        val context = Context()
        context.setVariable("userName", "${user.lastName} ${user.firstName} ${user.patronymic}")
        context.setVariable("userEmail", user.email)
        context.setVariable("loginUrl", "https://career-seekers.ru/login")
        context.setVariable("contactEmail", "kidschamp@adtspb.ru")

        val htmlContent = templateEngine.process("email-template", context)

        val message = mailer.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")
        helper.setTo(user.email)
        helper.setFrom(mailProperties.productionMail.username)
        helper.setSubject("Регистрация в системе Искатели профессий")
        helper.setText(htmlContent, true)

        val resource = ClassPathResource("static/images/logo.png")
        helper.addInline("logoImage", resource)

        mailer.send(message)
    }
}