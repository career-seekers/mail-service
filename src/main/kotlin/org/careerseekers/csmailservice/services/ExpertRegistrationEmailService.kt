package org.careerseekers.csmailservice.services

import org.careerseekers.csmailservice.cache.TemporaryPasswordsCache
import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.enums.MailEventTypes
import org.careerseekers.csmailservice.exceptions.BadRequestException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class ExpertRegistrationEmailService(
    @param:Qualifier("productionMailSender") override val mailer: JavaMailSender,
    private val temporaryPasswordsCache: TemporaryPasswordsCache,
) : EmailProcessingService {

    @Value("\${spring.mail.production_mail.username}")
    private val senderEmail: String? = null

    override val eventType = MailEventTypes.EXPERT_REGISTRATION

    override fun processEmail(message: EmailSendingTaskDto) {
        message.user?.let { user ->
            val cacheItem =
                temporaryPasswordsCache.getItemFromCache(user.email) ?: throw BadRequestException("Password not found")

            SimpleMailMessage().apply {
                from = senderEmail
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