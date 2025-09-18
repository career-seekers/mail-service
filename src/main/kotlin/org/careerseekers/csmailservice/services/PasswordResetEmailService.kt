package org.careerseekers.csmailservice.services

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.cache.VerificationCodesCache
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.dto.VerificationCodeDto
import org.careerseekers.csmailservice.enums.MailEventTypes
import org.careerseekers.csmailservice.exceptions.BadRequestException
import org.careerseekers.csmailservice.exceptions.NotFoundException
import org.careerseekers.csmailservice.services.interfaces.EmailProcessingService
import org.careerseekers.csmailservice.utils.CodeGenerator.generateVerificationCode
import org.careerseekers.csmailservice.utils.JwtUtil
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordResetEmailService(
    @param:Qualifier("productionMailSender") override val mailer: JavaMailSender,
    private val jwtUtil: JwtUtil,
    private val passwordEncoder: PasswordEncoder,
    private val verificationCodesCache: VerificationCodesCache,
    private val mailProperties: MailProperties,
) : EmailProcessingService {

    override val eventType = MailEventTypes.PASSWORD_RESET

    override fun handle(record: ConsumerRecord<String, EmailSendingTaskDto>) {
        val message = record.value()

        val user = message.token?.let {
            jwtUtil.getUserFromToken(it) ?: throw NotFoundException("User not found")
        } ?: message.user ?: throw BadRequestException("This method requires user")

        val code = generateVerificationCode()
        verificationCodesCache.loadItemToCache(
            VerificationCodeDto(
                userEmail = user.email,
                code = passwordEncoder.encode(code),
                retries = 0
            )
        )

        SimpleMailMessage().apply {
            from = mailProperties.productionMail.username
            setTo(user.email)
            subject = "Изменение пароля"
            text = """
            Уважаемый(-ая) ${user.lastName} ${user.firstName} ${user.patronymic}!
            Для подтверждения изменения пароля введите следующий верификационный код: $code
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