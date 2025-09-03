package org.careerseekers.csmailservice.config.kafka.consumers

import org.careerseekers.csmailservice.config.kafka.ConsumerFactoryConfiguration
import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.careerseekers.csmailservice.serializers.PolymorphicKafkaSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka

@Configuration
@EnableKafka
class EmailSendingConsumerConfig : ConsumerFactoryConfiguration() {

    @Bean
    fun emailSendingConsumerFactory() = createConsumerFactory<EmailSendingTaskDto>(
        groupId = "email_sending_tasks_consumer",
        valueDeserializer = PolymorphicKafkaSerializer::class.java,
    )

    @Bean
    fun emailSendingContainerFactory() = createKafkaListenerContainerFactory<EmailSendingTaskDto>(
        groupId = "email_sending_tasks_consumer",
        valueDeserializer = PolymorphicKafkaSerializer::class.java
    )
}
