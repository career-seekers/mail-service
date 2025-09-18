package org.careerseekers.csmailservice.services.kafka.consumers

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.dto.DirectionDocumentsTask
import org.careerseekers.csmailservice.services.interfaces.IDirectionDocsTasksProcessingService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class KafkaDirectionDocumentsTasksConsumer(
    private val directionDocsServices: List<IDirectionDocsTasksProcessingService>
) : CustomKafkaConsumer<String, DirectionDocumentsTask> {

    @KafkaListener(
        topics = ["DIRECTION_DOCUMENTS_TASKS"],
        groupId = "direction_docs_creation_consumer"
    )
    override fun receiveMessage(
        consumerRecord: ConsumerRecord<String, DirectionDocumentsTask>,
        acknowledgment: Acknowledgment
    ) {
        var counter = 0
        for (service in directionDocsServices) {
            if (service.eventType == consumerRecord.value().eventType) {
                service.handle(consumerRecord)
                counter++
            }
        }

        if (counter > 0) { acknowledgment.acknowledge() }
    }
}