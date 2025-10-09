package org.careerseekers.csmailservice.services.notifications

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.TgLinkNotificationDto
import org.careerseekers.csmailservice.services.interfaces.IKafkaMessageHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class TgLinksNotificationsService(
    @param:Qualifier("productionMailSender") private val mailer: JavaMailSender,
    private val mailProperties: MailProperties,
) : IKafkaMessageHandler<String, TgLinkNotificationDto> {

    override fun handle(record: ConsumerRecord<String, TgLinkNotificationDto>) {
        val message = record.value()
        val user = message.user

        SimpleMailMessage().apply {
            from = mailProperties.productionMail.username
            setTo(user.email)
            subject = "Добавление ссылки на Телеграм-аккаунт"
            text = """
            Уважаемый(-ая) ${user.lastName} ${user.firstName} ${user.patronymic}!   
            Сообщаем, что к Вашему аккаунту был привязан Telegram-аккаунт. Если это были Вы, ничего дополнительно предпринимать не нужно.

            Если Вы не осуществляли данное действие, пожалуйста, свяжитесь с нашей Службой поддержки для защиты Вашего аккаунта.
           
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