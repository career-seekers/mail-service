package org.careerseekers.csmailservice.services.notifications

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.enums.MailEventTypes
import org.careerseekers.csmailservice.services.interfaces.IEmailNotificationProcessingService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MentorAndUserRegistrationNotificationServiceNotificationI(
    @param:Qualifier("productionMailSender") override val mailer: JavaMailSender,
    private val mailProperties: MailProperties,
) : IEmailNotificationProcessingService {

    override val eventType = MailEventTypes.MENTOR_AND_USER_REGISTRATION

    override fun handle(record: ConsumerRecord<String, EmailSendingTaskDto>) {
        val message = record.value()

       message.user?.let { user ->
           SimpleMailMessage().apply {
               from = mailProperties.productionMail.username
               setTo(user.email)
               subject = "Регистрация наставника в системе Искатели профессий"
               text = """
            Уважаемый(-ая) ${user.lastName} ${user.firstName} ${user.patronymic}!
            Вы зарегистрировались как наставник чемпионата Искатели профессий. 
            По ссылке ниже Вы можете перейти в личный кабинет Наставника, где можете подробно изучить свои возможности:
            https://career-seekers.ru/login
            
            Спасибо,
            Команда поддержки Искателей профессий.
            ${mailProperties.productionMail.username}
            Канал технической поддержки платформы: https://t.me/career_seekers_help
        """.trimIndent()
           }.also {
               mailer.send(it)
           }
       }
    }
}