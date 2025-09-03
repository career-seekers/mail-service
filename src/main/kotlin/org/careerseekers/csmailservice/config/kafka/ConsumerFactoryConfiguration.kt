package org.careerseekers.csmailservice.config.kafka

import com.fasterxml.jackson.databind.deser.std.StringDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties

open class ConsumerFactoryConfiguration {

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var kafkaUrl: String

    protected fun <T> createConsumerFactory(
        groupId: String,
        valueDeserializer: Class<*>,
    ): ConsumerFactory<String, T> {
        val configProps = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaUrl,
            ConsumerConfig.GROUP_ID_CONFIG to groupId,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to valueDeserializer,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
            ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG to 1000,
            ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to 20000,
            ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG to 1000,
            ConsumerConfig.FETCH_MIN_BYTES_CONFIG to 1,
            ConsumerConfig.FETCH_MAX_BYTES_CONFIG to 52428800,
            ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG to 1048576,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG to 500,
            ConsumerConfig.ISOLATION_LEVEL_CONFIG to "read_committed"
        )

        return DefaultKafkaConsumerFactory(configProps)
    }

    protected fun <T> createKafkaListenerContainerFactory(
        groupId: String,
        valueDeserializer: Class<*>
    ): ConcurrentKafkaListenerContainerFactory<String, T> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, T>()

        factory.consumerFactory = createConsumerFactory(groupId, valueDeserializer)
        factory.containerProperties.ackMode =
            ContainerProperties.AckMode.MANUAL_IMMEDIATE

        return factory
    }
}