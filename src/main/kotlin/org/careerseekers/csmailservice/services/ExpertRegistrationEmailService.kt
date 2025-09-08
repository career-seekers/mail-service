package org.careerseekers.csmailservice.services

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.cache.TemporaryPasswordsCache
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.enums.MailEventTypes
import org.careerseekers.csmailservice.exceptions.BadRequestException
import org.careerseekers.csmailservice.services.kafka.EmailProcessingService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class ExpertRegistrationEmailService(
    @param:Qualifier("productionMailSender") override val mailer: JavaMailSender,
    private val temporaryPasswordsCache: TemporaryPasswordsCache,
    private val mailProperties: MailProperties,
) : EmailProcessingService {
    override val eventType = MailEventTypes.EXPERT_REGISTRATION

    override fun handle(record: ConsumerRecord<String, EmailSendingTaskDto>) {
        val message = record.value()

        message.user?.let { user ->
            val cacheItem =
                temporaryPasswordsCache.getItemFromCache(user.email) ?: throw BadRequestException("Password not found")

            SimpleMailMessage().apply {
                from = mailProperties.productionMail.username
                setTo(user.email)
                subject = "Регистрация эксперта в системе Искатели профессий"
                text = """
            Уважаемый(-ая) ${user.lastName} ${user.firstName} ${user.patronymic}!
            Вас зарегистрировали как Эксперта на чемпионат Искатели профессий. 
            По ссылке ниже вы можете перейти в личный кабинет Эксперта, где сможете подробно изучить свои возможности и обязанности:
            https://github.com/career-seekers
            
            Реквизиты для входа в личный кабинет:
            Логин: ${user.email}
            Пароль: ${cacheItem.password}
            
            Спасибо,
            Команда поддержки Искателей профессий.
        """.trimIndent()
            }.also {
                mailer.send(it)
            }
        }
    }
}