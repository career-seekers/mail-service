package org.careerseekers.csmailservice.services

import org.careerseekers.csmailservice.cache.VerificationCodesCache
import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.dto.VerificationCodeDto
import org.careerseekers.csmailservice.enums.MailEventTypes
import org.careerseekers.csmailservice.utils.CodeGenerator.generateVerificationCode
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PreRegistrationEmailService(
    @param:Qualifier("productionMailSender") override val mailer: JavaMailSender,
    private val verificationCodesCache: VerificationCodesCache,
    private val passwordEncoder: PasswordEncoder,
) : EmailProcessingService {

    @Value("\${spring.mail.production_mail.username}")
    private val senderEmail: String? = null

    override val eventType = MailEventTypes.PRE_REGISTRATION

    override fun processEmail(message: EmailSendingTaskDto) {
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
                from = senderEmail
                setTo(email)
                subject = "Регистрация в системе Искатели профессий"
                text = """
            Для завершения регистрации введите следующий верификационный код, он действителен в течение 5 минут: $code
            Если вы не запрашивали этот код, просто проигнорируйте это письмо.
            
            Спасибо,
            Команда поддержки Искателей профессий.
        """.trimIndent()
            }.also {
                mailer.send(it)
            }
        }
    }
}