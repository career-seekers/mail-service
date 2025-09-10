package org.careerseekers.csmailservice.services.kafka.consumers

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.dto.DirectionCreation
import org.careerseekers.csmailservice.services.DirectionCreationNotificationService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class KafkaDirectionCreationConsumer(private val service: DirectionCreationNotificationService) :
    CustomKafkaConsumer<String, DirectionCreation> {

    @KafkaListener(
        topics = ["DIRECTION_CREATION"],
        groupId = "direction_creation_consumer"
    )
    override fun receiveMessage(
        consumerRecord: ConsumerRecord<String, DirectionCreation>,
        acknowledgment: Acknowledgment
    ) {
        service.handle(consumerRecord)
        acknowledgment.acknowledge()
    }
}