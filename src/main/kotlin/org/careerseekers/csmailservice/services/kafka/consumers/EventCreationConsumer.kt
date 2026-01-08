package org.careerseekers.csmailservice.services.kafka.consumers

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.dto.EventCreationDto
import org.careerseekers.csmailservice.services.interfaces.IEventCreationProcessingService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class EventCreationConsumer(private val eventCreationProcessingServices: List<IEventCreationProcessingService>) :
    CustomKafkaConsumer<String, EventCreationDto> {

    @KafkaListener(
        topics = ["EVENT_CREATION"],
        groupId = "event_creation_consumer"
    )
    override fun receiveMessage(
        consumerRecord: ConsumerRecord<String, EventCreationDto>,
        acknowledgment: Acknowledgment
    ) {
        val message = consumerRecord.value()
        for (service in eventCreationProcessingServices) {
            if (service.eventType == message.eventType) {
                service.handle(consumerRecord)
                acknowledgment.acknowledge()

                break
            }
        }
    }
}