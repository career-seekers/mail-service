package org.careerseekers.csmailservice.dto

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.careerseekers.csmailservice.enums.MailEventTypes

@Serializable
@Polymorphic
sealed class KafkaMessagesDto : DtoClass

@Serializable
@SerialName("email_sending_task")
class EmailSendingTaskDto(
    val email: String? = null,
    val token: String? = null,
    val user: UsersCacheDto? = null,
    val eventType: MailEventTypes,
) : KafkaMessagesDto()