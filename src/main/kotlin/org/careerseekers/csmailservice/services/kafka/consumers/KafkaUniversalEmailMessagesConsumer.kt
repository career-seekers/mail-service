package org.careerseekers.csmailservice.services.kafka.consumers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.UniversalEmailMessageDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class KafkaUniversalEmailMessagesConsumer(
    @param:Qualifier("productionMailSender") private val mailer: JavaMailSender,
    private val mailProperties: MailProperties,
) : CustomKafkaConsumer<String, UniversalEmailMessageDto> {

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val logger = LoggerFactory.getLogger(KafkaUniversalEmailMessagesConsumer::class.java)

    @KafkaListener(
        topics = ["UNIVERSAL_EMAIL_MESSAGES_TOPIC"],
        groupId = "universal_email_messages_consumer"
    )
    override fun receiveMessage(
        consumerRecord: ConsumerRecord<String, UniversalEmailMessageDto>,
        acknowledgment: Acknowledgment
    ) {
        val message = consumerRecord.value()

        coroutineScope.launch {
            try {
                SimpleMailMessage().apply {
                    from = mailProperties.productionMail.username
                    setTo(message.email)
                    subject = "Добавление ссылки на Телеграм-аккаунт"
                    text = """
                    ${message.body}
                   
                    Спасибо,
                    Команда поддержки Искателей профессий. 
                    ${mailProperties.productionMail.username}
                    Канал технической поддержки платформы: https://t.me/career_seekers_help
                """.trimIndent()
                }.also {
                    mailer.send(it)
                }

                acknowledgment.acknowledge()
            } catch (e: Exception) {
                logger.error(e.message, e)
            }
        }
    }
}