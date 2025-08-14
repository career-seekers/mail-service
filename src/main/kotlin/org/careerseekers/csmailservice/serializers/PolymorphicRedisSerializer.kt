package org.careerseekers.csmailservice.serializers

import kotlinx.serialization.KSerializer
import org.careerseekers.csmailservice.dto.CachesDto
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.stereotype.Component
import kotlin.collections.isEmpty
import kotlin.collections.toString
import kotlin.text.toByteArray

@Suppress("UNCHECKED_CAST")
@Component
class PolymorphicRedisSerializer<Base : CachesDto> : RedisSerializer<Base> {

    private val json = CustomSerializerModule.json
    private val baseSerializer = CachesDto.serializer() as KSerializer<Base>

    override fun serialize(t: Base?): ByteArray? {
        if (t == null) return null
        return json.encodeToString(baseSerializer, t).toByteArray(Charsets.UTF_8)
    }

    override fun deserialize(bytes: ByteArray?): Base? {
        if (bytes == null || bytes.isEmpty()) return null
        return json.decodeFromString(baseSerializer, bytes.toString(Charsets.UTF_8))
    }
}