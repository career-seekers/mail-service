package org.careerseekers.csmailservice.dto

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.careerseekers.csmailservice.enums.DirectionDocsEventTypes
import org.careerseekers.csmailservice.enums.EventTypes
import org.careerseekers.csmailservice.enums.MailEventTypes
import org.careerseekers.csmailservice.enums.ParticipantStatus

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
data class DirectionDocumentsTask (
    val eventType: DirectionDocsEventTypes,
    val documentType: String,
    val directionName: String,
    val ageCategory: String,
    val expert: UsersCacheDto,
    val tutor: UsersCacheDto,
    val verification: Boolean,
) : KafkaMessagesDto()

@Serializable
@SerialName("UniversalEmailMessage")
data class UniversalEmailMessageDto(
    val email: String,
    val subject: String,
    val body: String,
) : KafkaMessagesDto()

@Serializable
@SerialName("ParticipantStatusUpdate")
data class ParticipantStatusUpdate (
    val status: ParticipantStatus,
    val user: UsersCacheDto,
    val mentor: UsersCacheDto,
    val childName: String,
    val competitionName: String,
    val ageCategory: String,
) : KafkaMessagesDto()

@Serializable
@SerialName("EventCreationDto")
data class EventCreationDto(
    val eventType: EventTypes,
    val directionName: String,
    val ageCategory: String,
    val expertName: String,
    val expertEmail: String,
    val participantsEmailList: List<String>
) : KafkaMessagesDto()