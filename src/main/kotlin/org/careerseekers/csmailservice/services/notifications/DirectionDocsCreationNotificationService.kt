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
class DirectionDocsCreationNotificationService(
    @param:Qualifier("productionMailSender") private val mailer: JavaMailSender,
    private val mailProperties: MailProperties,
) : IDirectionDocsTasksProcessingService {

    override val eventType = DirectionDocsEventTypes.CREATION

    override fun handle(record: ConsumerRecord<String, DirectionDocumentsTask>) {
        val message = record.value()

        SimpleMailMessage().apply {
            from = mailProperties.productionMail.username
            setTo(message.tutor.email)
            subject = "Искатели Профессий | Загрузка нового документа"
            text = """
            Уважаемый(-ая) ${message.tutor.lastName} ${message.tutor.firstName} ${message.tutor.patronymic}!   
            Сообщаем Вам, что ${message.expert.lastName} ${message.expert.firstName} ${message.expert.patronymic} добавил новый документ для компетенции ${message.directionName} возрастной категории ${message.ageCategory}.

            Более подробную информацию Вы можете увидеть в своем Личном кабинете.
           
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