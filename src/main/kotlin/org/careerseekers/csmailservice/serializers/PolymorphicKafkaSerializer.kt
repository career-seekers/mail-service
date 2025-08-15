package org.careerseekers.csmailservice.serializers

import kotlinx.serialization.KSerializer
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import org.careerseekers.csmailservice.dto.KafkaMessagesDto
import org.springframework.stereotype.Component
import kotlin.collections.isEmpty
import kotlin.collections.toString
import kotlin.text.toByteArray

@Suppress("UNCHECKED_CAST")
@Component
class PolymorphicKafkaSerializer<Base : KafkaMessagesDto> : Serializer<Base>, Deserializer<Base> {

    private val json = CustomSerializerModule.json
    private val baseSerializer = KafkaMessagesDto.serializer() as KSerializer<Base>

    override fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {}

    override fun close() {}

    override fun deserialize(topic: String, data: ByteArray?): Base? {
        if (data == null || data.isEmpty()) return null
        return json.decodeFromString(baseSerializer, data.toString(Charsets.UTF_8))
    }

    override fun serialize(topic: String, data: Base?): ByteArray? {
        if (data == null) return null
        return json.encodeToString(baseSerializer, data).toByteArray(Charsets.UTF_8)
    }
}
