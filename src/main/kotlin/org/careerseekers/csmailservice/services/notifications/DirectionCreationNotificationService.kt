package org.careerseekers.csmailservice.services.notifications

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.DirectionCreation
import org.careerseekers.csmailservice.services.interfaces.IKafkaMessageHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class DirectionCreationNotificationService(
    @param:Qualifier("productionMailSender") private val mailer: JavaMailSender,
    private val mailProperties: MailProperties,
) : IKafkaMessageHandler<String, DirectionCreation> {

    override fun handle(record: ConsumerRecord<String, DirectionCreation>) {
        val message = record.value()

        SimpleMailMessage().apply {
            from = mailProperties.productionMail.username
            setTo(message.expert.email)
            subject = "Искатели Профессий | Создание новой компетенции"
            text = """
            Уважаемый(-ая) ${message.expert.lastName} ${message.expert.firstName} ${message.expert.patronymic}!   
            Сообщаем, что Вам добавлена новая компетенция — ${message.name}.

            Более подробную информацию Вы можете увидеть в своем Личном кабинете.
           
            Спасибо,
            Команда поддержки Искателей профессий.
            kidschamp@adtspb.ru
            Канал технической поддержки платформы: https://t.me/career_seekers_help
        """.trimIndent()
        }.also {
            mailer.send(it)
        }
    }
}