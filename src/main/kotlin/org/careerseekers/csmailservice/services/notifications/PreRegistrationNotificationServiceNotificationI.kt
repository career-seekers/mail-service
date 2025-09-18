package org.careerseekers.csmailservice.services.notifications

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.cache.VerificationCodesCache
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.dto.VerificationCodeDto
import org.careerseekers.csmailservice.enums.MailEventTypes
import org.careerseekers.csmailservice.services.interfaces.IEmailNotificationProcessingService
import org.careerseekers.csmailservice.utils.CodeGenerator.generateVerificationCode
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PreRegistrationNotificationServiceNotificationI(
    @param:Qualifier("productionMailSender") override val mailer: JavaMailSender,
    private val verificationCodesCache: VerificationCodesCache,
    private val passwordEncoder: PasswordEncoder,
    private val mailProperties: MailProperties,
) : IEmailNotificationProcessingService {

    override val eventType = MailEventTypes.PRE_REGISTRATION

    override fun handle(record: ConsumerRecord<String, EmailSendingTaskDto>) {
        val message = record.value()

        message.email?.let { email ->
            val code = generateVerificationCode()
            verificationCodesCache.loadItemToCache(
                VerificationCodeDto(
                    userEmail = email,
                    code = passwordEncoder.encode(code),
                    retries = 0
                )
            )

            SimpleMailMessage().apply {
                from = mailProperties.productionMail.username
                setTo(email)
                subject = "Регистрация в системе Искатели профессий"
                text = """
            Для завершения регистрации введите следующий верификационный код, он действителен в течение 5 минут: $code
            Если Вы не запрашивали этот код, просто проигнорируйте это письмо.
            
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
}