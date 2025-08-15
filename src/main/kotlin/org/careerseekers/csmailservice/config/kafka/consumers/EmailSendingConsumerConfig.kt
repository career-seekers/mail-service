package org.careerseekers.csmailservice.config.kafka.consumers

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.serializers.PolymorphicKafkaSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties

@Configuration
@EnableKafka
class EmailSendingConsumerConfig {
    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var kafkaUrl: String

    @Bean
    fun emailSendingConsumerFactory(): ConsumerFactory<String, EmailSendingTaskDto> {
        val configProps = mapOf(
            /**
             *  Kafka cluster connection settings
             *  Connecting to Kafka and serializing keys and values
             */
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaUrl,
            ConsumerConfig.GROUP_ID_CONFIG to "email_sending_tasks_consumer",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to PolymorphicKafkaSerializer::class.java,

            /**
             *  Auto commit configuration
             */
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
            ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG to 1000,

            /**
             *  Session and heartbeat settings
             */
            ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to 20000,
            ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG to 1000,

            /**
             *  Fetch settings
             */
            ConsumerConfig.FETCH_MIN_BYTES_CONFIG to 1,
            ConsumerConfig.FETCH_MAX_BYTES_CONFIG to 52428800, // 50MB
            ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG to 1048576, // 1MB

            /**
             * Offset reset policy
             */
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",

            /**
             *  Max poll records
             */
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG to 500,

            /**
             * Isolation level for transactions
             */
            ConsumerConfig.ISOLATION_LEVEL_CONFIG to "read_committed"
        )


        return DefaultKafkaConsumerFactory(configProps)
    }

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, EmailSendingTaskDto>
    ): ConcurrentKafkaListenerContainerFactory<String, EmailSendingTaskDto> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, EmailSendingTaskDto>()

        factory.consumerFactory = consumerFactory
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL_IMMEDIATE

        return factory
    }
}
