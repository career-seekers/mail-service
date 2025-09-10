package org.careerseekers.csmailservice.serializers

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.careerseekers.csmailservice.dto.CachesDto
import org.careerseekers.csmailservice.dto.DirectionCreation
import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.dto.KafkaMessagesDto
import org.careerseekers.csmailservice.dto.PlatformCreationDto
import org.careerseekers.csmailservice.dto.TgLinkNotificationDto
import org.careerseekers.csmailservice.dto.UsersCacheDto

object CustomSerializerModule {
    val customSerializerModule = SerializersModule {
        polymorphic(CachesDto::class) {
            subclass(UsersCacheDto::class, UsersCacheDto.serializer())
        }
        polymorphic(KafkaMessagesDto::class) {
            subclass(EmailSendingTaskDto::class, EmailSendingTaskDto.serializer())
            subclass(PlatformCreationDto::class, PlatformCreationDto.serializer())
            subclass(TgLinkNotificationDto::class, TgLinkNotificationDto.serializer())
            subclass(DirectionCreation::class, DirectionCreation.serializer())
        }
    }

    val json = Json {
        serializersModule = customSerializerModule
        classDiscriminator = "type"
        ignoreUnknownKeys = true
    }
}