package org.careerseekers.csmailservice.services

import org.careerseekers.csmailservice.cache.VerificationCodesCache
import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.dto.VerificationCodeDto
import org.careerseekers.csmailservice.enums.MailEventTypes
import org.careerseekers.csmailservice.exceptions.BadRequestException
import org.careerseekers.csmailservice.exceptions.NotFoundException
import org.careerseekers.csmailservice.utils.CodeGenerator.generateVerificationCode
import org.careerseekers.csmailservice.utils.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordResetEmailService(
    private val jwtUtil: JwtUtil,
    private val mailer: JavaMailSender,
    private val passwordEncoder: PasswordEncoder,
    private val verificationCodesCache: VerificationCodesCache
) : EmailProcessingService {
    @Value("\${spring.mail.username}")
    private val senderEmail: String? = null

    override val eventType = MailEventTypes.PASSWORD_RESET

    override fun processEmail(message: EmailSendingTaskDto) {
        val user = message.token?.let {
            jwtUtil.getUserFromToken(it) ?: throw NotFoundException("User not found")
        } ?: message.user ?: throw BadRequestException("This method requires user")

        val code = generateVerificationCode()
        verificationCodesCache.loadItemToCache(
            VerificationCodeDto(
                userId = user.id,
                code = passwordEncoder.encode(code),
                retries = 0
            )
        )

        SimpleMailMessage().apply {
            from = senderEmail
            setTo(user.email)
            subject = "Изменение пароля"
            text = """
            Уважаемый(-ая) ${user.lastName} ${user.firstName} ${user.patronymic}!
            Для подтверждения изменения пароля введите следующий верификационный код: $code
            Если вы не запрашивали этот код, просто проигнорируйте это письмо.
            
            Спасибо,
            Команда поддержки Искателей профессий.
        """.trimIndent()
        }.also {
            mailer.send(it)
        }
    }
}