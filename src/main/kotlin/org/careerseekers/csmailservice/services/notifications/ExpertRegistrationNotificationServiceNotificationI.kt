package org.careerseekers.csmailservice.services.notifications

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.cache.TemporaryPasswordsCache
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.enums.MailEventTypes
import org.careerseekers.csmailservice.exceptions.BadRequestException
import org.careerseekers.csmailservice.services.interfaces.IEmailNotificationProcessingService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class ExpertRegistrationNotificationServiceNotificationI(
    @param:Qualifier("productionMailSender") override val mailer: JavaMailSender,
    private val temporaryPasswordsCache: TemporaryPasswordsCache,
    private val mailProperties: MailProperties,
    private val templateEngine: TemplateEngine
) : IEmailNotificationProcessingService {
    override val eventType = MailEventTypes.EXPERT_REGISTRATION

    override fun handle(record: ConsumerRecord<String, EmailSendingTaskDto>) {
        val kafkaMessage = record.value()
        val user = kafkaMessage.user!!

        val cacheItem =
            temporaryPasswordsCache.getItemFromCache(user.email) ?: throw BadRequestException("Password not found")

        val context = Context()
        context.setVariable("userName", "${user.lastName} ${user.firstName} ${user.patronymic}")
        context.setVariable("userEmail", user.email)
        context.setVariable("temporaryPassword", cacheItem.password)
        context.setVariable("loginUrl", "https://career-seekers.ru/login")
        context.setVariable("contactEmail", "kidschamp@adtspb.ru")

        val htmlContent = templateEngine.process("expert-email-template", context)

        val message = mailer.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")
        helper.setTo(user.email)
        helper.setFrom(mailProperties.productionMail.username)
        helper.setSubject("Регистрация Главного эксперта в системе Искатели профессий")
        helper.setText(htmlContent, true)

        val resource = ClassPathResource("static/images/logo.png")
        helper.addInline("logoImage", resource)

        mailer.send(message)
    }
}