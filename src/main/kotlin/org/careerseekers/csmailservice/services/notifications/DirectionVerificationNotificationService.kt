package org.careerseekers.csmailservice.services.notifications

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.DirectionDocumentsTask
import org.careerseekers.csmailservice.enums.DirectionDocsEventTypes
import org.careerseekers.csmailservice.services.interfaces.IDirectionDocsTasksProcessingService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class DirectionVerificationNotificationService(
    @param:Qualifier("productionMailSender") private val mailer: JavaMailSender,
    private val mailProperties: MailProperties,
) : IDirectionDocsTasksProcessingService {

    override val eventType = DirectionDocsEventTypes.VERIFICATION

    override fun handle(record: ConsumerRecord<String, DirectionDocumentsTask>) {
        val message = record.value()

        SimpleMailMessage().apply {
            from = mailProperties.productionMail.username
            setTo(message.expert.email)
            subject = "Искатели Профессий | Информация о статусе документа"

            text = if (message.verification) {
                generateSuccessEmailText(message)
            } else {
                generateFailureEmailText(message)
            }
        }.also {
            mailer.send(it)
        }
    }

    private fun generateSuccessEmailText(message: DirectionDocumentsTask): String {
        return """
            Уважаемый(-ая) ${message.expert.lastName} ${message.expert.firstName} ${message.expert.patronymic}!   
            Сообщаем Вам, что документ «${message.documentType}» для компетенции ${message.directionName} возрастной категории ${message.ageCategory} успешно прошел проверку.
            
            Спасибо,
            Команда поддержки Искателей профессий.
            ${mailProperties.productionMail.username}
            Канал технической поддержки платформы: https://t.me/career_seekers_help
        """.trimIndent()
    }

    private fun generateFailureEmailText(message: DirectionDocumentsTask): String {
        return """
            Уважаемый(-ая) ${message.expert.lastName} ${message.expert.firstName} ${message.expert.patronymic}!   
            Сообщаем Вам, что документ «${message.documentType}» для компетенции ${message.directionName} возрастной категории ${message.ageCategory} не прошел проверку.
                    
            Для уточнения деталей, пожалуйста, обратитесь к Надежде Анатольевне Бурдун.
            Адрес электронной почты для связи: kidschamp@adtspb.ru
      
                    
            Спасибо,
            Команда поддержки Искателей профессий.
            ${mailProperties.productionMail.username}
            Канал технической поддержки платформы: https://t.me/career_seekers_help
        """.trimIndent()
    }
}