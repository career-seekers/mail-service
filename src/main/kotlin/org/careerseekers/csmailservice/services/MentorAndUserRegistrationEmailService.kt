package org.careerseekers.csmailservice.services

import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.enums.MailEventTypes
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MentorAndUserRegistrationEmailService(
    @param:Qualifier("productionMailSender") override val mailer: JavaMailSender
) : EmailProcessingService {

    @Value("\${spring.mail.production_mail.username}")
    private val senderEmail: String? = null

    override val eventType = MailEventTypes.MENTOR_AND_USER_REGISTRATION

    override fun processEmail(message: EmailSendingTaskDto) {
       message.user?.let { user ->
           SimpleMailMessage().apply {
               from = senderEmail
               setTo(user.email)
               subject = "Регистрация наставника в системе Искатели профессий"
               text = """
            Уважаемый(-ая) ${user.lastName} ${user.firstName} ${user.patronymic}!
            Вы зарегистрировались как наставник чемпионата Искатели профессий. 
            По ссылке ниже вы можете перейти в личный кабинет Наставника, где можете подробно изучить свои возможности и обязанности:
            https://github.com/career-seekers
            
            Спасибо,
            Команда поддержки Искателей профессий.
        """.trimIndent()
           }.also {
               mailer.send(it)
           }
       }
    }
}