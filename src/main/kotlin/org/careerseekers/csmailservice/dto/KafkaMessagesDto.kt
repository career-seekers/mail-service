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

@Serializable
data class PlatformDto(
    val id: Long,
    val fullName: String,
    val shortName: String,
    val address: String,
    val userId: Long?,
)

@Serializable
@SerialName("PlatformCreation")
data class PlatformCreationDto (
    val platform: PlatformDto
) : KafkaMessagesDto()

@Serializable
@SerialName("tg_link_notification")
class TgLinkNotificationDto(
    val user: UsersCacheDto,
    val eventType: MailEventTypes = MailEventTypes.TG_LINK_CREATION,
) : KafkaMessagesDto()

@Serializable
@SerialName("DirectionCreation")
data class DirectionCreation(
    val name: String,
    val tutor: UsersCacheDto,
    val expert: UsersCacheDto,
) : KafkaMessagesDto()

@Serializable
@SerialName("DirectionDocumentsCreation")
data class DirectionDocumentsCreation (
    val documentType: String,
    val directionName: String,
    val directionAgeCategory: String,
    val expert: UsersCacheDto,
    val tutor: UsersCacheDto,
) : KafkaMessagesDto()