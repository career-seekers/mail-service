package org.careerseekers.csmailservice.services.notifications

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.cache.UsersCacheClient
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.PlatformCreationDto
import org.careerseekers.csmailservice.services.interfaces.IKafkaMessageHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class PlatformCreationNotificationService(
    @param:Qualifier("productionMailSender") private val mailer: JavaMailSender,
    private val usersCacheClient: UsersCacheClient,
    private val mailProperties: MailProperties,
) : IKafkaMessageHandler<String, PlatformCreationDto> {

    override fun handle(record: ConsumerRecord<String, PlatformCreationDto>) {
        val message = record.value()
        val user = message.platform.userId?.let { usersCacheClient.getItemFromCache(it) }

        SimpleMailMessage().apply {
            from = mailProperties.serviceMail.username
            setTo(mailProperties.productionMail.username)
            subject = "Регистрация новой площадки"

            if (user != null) {
                text = """
                Добавлена заявка на регистрацию площадки.
                
                Полное название: ${message.platform.fullName}
                Краткое название: ${message.platform.shortName}
                Адрес размещения: ${message.platform.address}
                
                Информация о пользователе, добавившем площадку
                ФИО: ${user.lastName} ${user.firstName} ${user.patronymic}
                Email: ${user.email}
                Телефон для связи: ${user.mobileNumber}

                
                С уважением,
                Служба информирования Искателей профессий 
                """.trimIndent()
            } else {
                text = """
                Добавлена заявка на регистрацию площадки.
                
                Полное название: ${message.platform.fullName}
                Краткое название: ${message.platform.shortName}
                Адрес размещения: ${message.platform.address}
                
                Информация о пользователе не предоставлена.
                

                Спасибо,
                Команда поддержки Искателей профессий.
                kidschamp@adtspb.ru
                Канал технической поддержки платформы: https://t.me/career_seekers_help
                """.trimIndent()
            }
        }.also { mailer.send(it) }
    }
}