package org.careerseekers.csmailservice.dto

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.careerseekers.csmailservice.enums.UsersRoles
import org.careerseekers.csmailservice.serializers.DateSerializer
import java.util.Date

@Polymorphic
@Serializable
sealed class CachesDto : DtoClass

@Serializable
@SerialName("UsersCacheDto")
data class UsersCacheDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    @Serializable(with = DateSerializer::class)
    val dateOfBirth: Date?,
    val email: String,
    val mobileNumber: String,
    val password: String,
    val role: UsersRoles,
    val avatarId: Long,
    val verified: Boolean,
    val isMentor: Boolean,
) : CachesDto()

@Serializable
@SerialName("VerificationCodeDto")
data class VerificationCodeDto(
    val userEmail: String,
    val code: String,
    var retries: Int
) : CachesDto()

@Serializable
@SerialName("TemporaryPasswordDto")
data class TemporaryPasswordDto(
    val email: String,
    val password: String
) : CachesDto()

val cacheModule = SerializersModule {
    polymorphic(CachesDto::class) {
        subclass(UsersCacheDto::class, UsersCacheDto.serializer())
        subclass(VerificationCodeDto::class, VerificationCodeDto.serializer())
        subclass(TemporaryPasswordDto::class, TemporaryPasswordDto.serializer())
    }
}

val json = Json {
    serializersModule = cacheModule
    classDiscriminator = "type"
    ignoreUnknownKeys = true
}