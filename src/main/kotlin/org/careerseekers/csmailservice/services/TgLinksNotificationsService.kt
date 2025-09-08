package org.careerseekers.csmailservice.services

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.TgLinkNotificationDto
import org.careerseekers.csmailservice.services.kafka.KafkaMessageHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class TgLinksNotificationsService(
    @param:Qualifier("productionMailSender") private val mailer: JavaMailSender,
    private val mailProperties: MailProperties,
) : KafkaMessageHandler<String, TgLinkNotificationDto> {

    override fun handle(record: ConsumerRecord<String, TgLinkNotificationDto>) {
        val message = record.value()
        val user = message.user

        SimpleMailMessage().apply {
            from = mailProperties.productionMail.username
            setTo(user.email)
            subject = "Добавление ссылки на Телеграм-аккаунт"
            text = """
            Уважаемы(-ая) ${user.lastName} ${user.firstName} ${user.patronymic}!   
            Сообщаем, что к Вашему аккаунту был привязан Telegram-аккаунт. Если это были Вы, ничего дополнительно предпринимать не нужно.

            Если вы не осуществляли данное действие, пожалуйста, свяжитесь с нашей Службой поддержки для защиты Вашего аккаунта.
           
            Спасибо,
            Команда поддержки Искателей профессий.
            ${mailProperties.productionMail.username}
        """.trimIndent()
        }.also {
            mailer.send(it)
        }
    }
}