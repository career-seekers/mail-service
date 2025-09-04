package org.careerseekers.csmailservice.services.kafka.consumers

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.dto.PlatformCreationDto
import org.careerseekers.csmailservice.services.PlatformCreationService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class KafkaPlatformCreationConsumer(
    private val platformCreationService: PlatformCreationService
) : CustomKafkaConsumer<String, PlatformCreationDto> {

    @KafkaListener(
        topics = ["PLATFORM_CREATION"],
        groupId = "platform_creations_tasks_consumer",
    )
    override fun receiveMessage(
        consumerRecord: ConsumerRecord<String, PlatformCreationDto>,
        acknowledgment: Acknowledgment
    ) {
        platformCreationService.handle(consumerRecord)
        acknowledgment.acknowledge()
    }
}