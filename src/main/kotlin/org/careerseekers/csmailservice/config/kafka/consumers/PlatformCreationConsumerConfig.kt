package org.careerseekers.csmailservice.config.kafka.consumers

import org.careerseekers.csmailservice.config.kafka.ConsumerFactoryConfiguration
import org.careerseekers.csmailservice.dto.PlatformCreationDto
import org.careerseekers.csmailservice.serializers.PolymorphicKafkaSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka

@Configuration
@EnableKafka
class PlatformCreationConsumerConfig : ConsumerFactoryConfiguration() {

    @Bean
    fun platformCreationConsumerFactory() = createConsumerFactory<PlatformCreationDto>(
        groupId = "platform_creations_tasks_consumer",
        valueDeserializer = PolymorphicKafkaSerializer::class.java,
    )

    @Bean
    fun platformCreationContainerFactory() = createKafkaListenerContainerFactory<PlatformCreationDto>(
        groupId = "platform_creations_tasks_consumer",
        valueDeserializer = PolymorphicKafkaSerializer::class.java,
    )
}