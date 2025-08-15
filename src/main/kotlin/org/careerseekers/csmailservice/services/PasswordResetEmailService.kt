package org.careerseekers.csmailservice.services

import org.careerseekers.csmailservice.cache.VerificationCodesCache
import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.dto.VerificationCodeDto
import org.careerseekers.csmailservice.enums.MailEventTypes
import org.careerseekers.csmailservice.exceptions.NotFoundException
import org.careerseekers.csmailservice.utils.CodeGenerator.generateVerificationCode
import org.careerseekers.csmailservice.utils.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class PasswordResetEmailService(
    private val mailer: JavaMailSender,
    private val jwtUtil: JwtUtil,
    private val verificationCodesCache: VerificationCodesCache
) : EmailProcessingService {
    @Value("\${spring.mail.username}")
    private val senderEmail: String? = null

    override val eventType = MailEventTypes.PASSWORD_RESET

    override fun processEmail(message: EmailSendingTaskDto) {
        jwtUtil.getUserFromToken(message.token)?.let { user ->
            val code = generateVerificationCode()
            verificationCodesCache.loadItemToCache(VerificationCodeDto(
                userId = user.id,
                code = code,
                retries = 0
            ))

            val message = SimpleMailMessage()

            message.from = senderEmail
            message.setTo(user.email)
            message.subject = "Изменение пароля"
            message.text = """
               Уважаемый(-ая) ${user.patronymic} ${user.firstName} ${user.lastName}!
               
               Для подтверждения изменения пароля введите следующий верификационный:
               
               $code
               
               Если вы не запрашивали этот код, просто проигнорируйте это письмо.

               Спасибо,
               Команда поддержки Искателей профессий.
            """.trimIndent()

            mailer.send(message)
        } ?: throw NotFoundException("User not found")

    }
}