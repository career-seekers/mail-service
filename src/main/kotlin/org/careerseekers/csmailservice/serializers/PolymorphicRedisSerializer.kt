package org.careerseekers.csmailservice.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.springframework.data.redis.serializer.RedisSerializer
import kotlin.collections.isEmpty
import kotlin.collections.toString
import kotlin.text.toByteArray

class PolymorphicRedisSerializer<Base : Any>(
    private val baseSerializer: KSerializer<Base>,
    private val json: Json
) : RedisSerializer<Base> {

    override fun serialize(t: Base?): ByteArray? {
        if (t == null) return null
        return json.encodeToString(baseSerializer, t).toByteArray(Charsets.UTF_8)
    }

    override fun deserialize(bytes: ByteArray?): Base? {
        if (bytes == null || bytes.isEmpty()) return null
        return json.decodeFromString(baseSerializer, bytes.toString(Charsets.UTF_8))
    }
}