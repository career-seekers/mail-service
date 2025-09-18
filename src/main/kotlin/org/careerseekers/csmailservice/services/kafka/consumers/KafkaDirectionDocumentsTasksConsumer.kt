package org.careerseekers.csmailservice.services.kafka.consumers

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.dto.DirectionDocumentsCreation
import org.careerseekers.csmailservice.services.DirectionDocumentsTasksNotificationService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class KafkaDirectionDocumentsTasksConsumer(
    private val service: DirectionDocumentsTasksNotificationService
) : CustomKafkaConsumer<String, DirectionDocumentsCreation> {

    @KafkaListener(
        topics = ["DIRECTION_DOCUMENTS_TASKS"],
        groupId = "direction_docs_creation_consumer"
    )
    override fun receiveMessage(
        consumerRecord: ConsumerRecord<String, DirectionDocumentsCreation>,
        acknowledgment: Acknowledgment
    ) {
        service.handle(consumerRecord)
        acknowledgment.acknowledge()
    }
}