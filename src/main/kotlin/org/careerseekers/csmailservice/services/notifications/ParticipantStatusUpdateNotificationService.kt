package org.careerseekers.csmailservice.services.notifications

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.config.MailProperties
import org.careerseekers.csmailservice.dto.ParticipantStatusUpdate
import org.careerseekers.csmailservice.dto.UsersCacheDto
import org.careerseekers.csmailservice.enums.ParticipantStatus
import org.careerseekers.csmailservice.services.interfaces.IKafkaMessageHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class ParticipantStatusUpdateNotificationService(
    @param:Qualifier("productionMailSender") val mailer: JavaMailSender,
    private val mailProperties: MailProperties,
    private val templateEngine: TemplateEngine,
) : IKafkaMessageHandler<String, ParticipantStatusUpdate> {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun handle(record: ConsumerRecord<String, ParticipantStatusUpdate>) {
        val kafkaMessage = record.value()
        val user = kafkaMessage.user
        val mentor = if (kafkaMessage.mentor.email != kafkaMessage.user.email) kafkaMessage.mentor else null

        sendMail(user, kafkaMessage)
        mentor?.let { sendMail(it, kafkaMessage) }
    }

    private fun sendMail(user: UsersCacheDto, kafkaMessage: ParticipantStatusUpdate) {
        val context = Context()
        context.setVariable("notificationTitle", "Изменение статуса участия в Чемпионате")
        context.setVariable("userName", "${user.lastName} ${user.firstName} ${user.patronymic}")
        context.setVariable("contactEmail", "kidschamp@adtspb.ru")
        if (kafkaMessage.status == ParticipantStatus.FINALIST) {
            context.setVariable(
                "message", """
                <p>
                    Поздравляем, ${kafkaMessage.childName} стал(-a) финалистом четвёртого регионального чемпионата «Искатели профессий» в компетенции ${kafkaMessage.competitionName} ${kafkaMessage.ageCategory}!
                </p>
                <br>
                <p>
                    Ждем вас на образовательном этапе, который пройдет с 1 по 28 февраля 2026 года, расписание появится на сайте чемпионата до 1 февраля: https://career-seekers.tilda.ws/.
                </p>
                <br>
                <p>
                    А также на финальном этапе, который пройдет с 14 по 28 февраля, точную дату и время позже вышлет главный эксперт компетенции.
                    Результаты отборочного этапа уже опубликованы на сайте чемпионата.
                </p>
                <br>
                <p>
                    1 декабря на сайте чемпионата будут опубликованы сертификаты конкурсантов и наставников отборочного этапа чемпионата - https://career-seekers.tilda.ws/.  
                    До встречи!
                </p>
                """.trimIndent()
            )
        } else if (kafkaMessage.status == ParticipantStatus.PARTICIPANT) {
            context.setVariable(
                "message", """
                <p>
                    Благодарим, ${kafkaMessage.childName} за участие в четвёртом региональном чемпионате «Искатели профессий» в компетенции ${kafkaMessage.competitionName} ${kafkaMessage.ageCategory}!
                </p>
                <br>
                <p>
                    Ваш результат не стал призовым, но мы верим, что стоит попробовать в следующий раз и вас наверняка ждёт успех!
                </p>
                <br>
                <p>
                    1 декабря на сайте чемпионата будут опубликованы сертификаты конкурсантов и наставников отборочного этапа чемпионата - https://career-seekers.tilda.ws/.  
                    До новых встреч!
                </p>
                """.trimIndent()
            )
        }

        val htmlContent = templateEngine.process("participant-status-update-notification", context)

        val message = mailer.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")
        helper.setTo(user.email)
        helper.setFrom(mailProperties.productionMail.username)
        helper.setSubject("Изменение статуса участия в Чемпионате")
        helper.setText(htmlContent, true)

        val resource = ClassPathResource("static/images/logo.png")
        helper.addInline("logoImage", resource)

        coroutineScope.launch {
            mailer.send(message)
            logger.debug("Mail notification sent successfully.")
        }
    }
}