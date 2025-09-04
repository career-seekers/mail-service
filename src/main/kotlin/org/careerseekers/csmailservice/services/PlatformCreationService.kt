package org.careerseekers.csmailservice.services

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.cache.UsersCacheClient
import org.careerseekers.csmailservice.dto.PlatformCreationDto
import org.careerseekers.csmailservice.dto.UsersCacheDto
import org.careerseekers.csmailservice.services.kafka.KafkaMessageHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class PlatformCreationService(
    @param:Qualifier("serviceMailSender") private val mailer: JavaMailSender,
    private val usersCacheClient: UsersCacheClient,
) : KafkaMessageHandler<String, PlatformCreationDto> {

    @Value("\${spring.mail.service_mail.username}")
    private val senderEmail: String? = null

    @Value("\${spring.mail.production_mail.username}")
    private val consumerEmail: String? = null

    override fun handle(record: ConsumerRecord<String, PlatformCreationDto>) {
        val message = record.value()

        var user: UsersCacheDto? = null
        message.platform.userId?.let { user = usersCacheClient.getItemFromCache(it) }

        SimpleMailMessage().apply {
            from = senderEmail
            setTo(consumerEmail)
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
                

                С уважением,
                Служба информирования Искателей профессий
                """.trimIndent()
            }
        }.also { mailer.send(it) }
    }
}