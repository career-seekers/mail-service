package org.careerseekers.csmailservice.services.kafka.consumers

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.dto.ParticipantStatusUpdate
import org.careerseekers.csmailservice.services.notifications.ParticipantStatusUpdateNotificationService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class KafkaParticipantStatusUpdateConsumer(
    private val notificationService: ParticipantStatusUpdateNotificationService,
) : CustomKafkaConsumer<String, ParticipantStatusUpdate> {

    @KafkaListener(
        topics = ["PARTICIPATION_STATUS_UPDATE"],
        groupId = "participation_status_update_consumers_group"
    )
    override fun receiveMessage(
        consumerRecord: ConsumerRecord<String, ParticipantStatusUpdate>,
        acknowledgment: Acknowledgment
    ) {
        notificationService.handle(consumerRecord)
        acknowledgment.acknowledge()
    }
}