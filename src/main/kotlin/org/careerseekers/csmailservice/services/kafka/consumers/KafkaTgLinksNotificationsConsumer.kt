package org.careerseekers.csmailservice.services.kafka.consumers

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.dto.TgLinkNotificationDto
import org.careerseekers.csmailservice.services.TgLinksNotificationsService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class KafkaTgLinksNotificationsConsumer(
    private val emailProcessingService: TgLinksNotificationsService,
) : CustomKafkaConsumer<String, TgLinkNotificationDto> {

    @KafkaListener(
        topics = ["TG_LINKS_NOTIFICATION"],
        groupId = "tg_links_notifications_consumer"
    )
    override fun receiveMessage(
        consumerRecord: ConsumerRecord<String, TgLinkNotificationDto>,
        acknowledgment: Acknowledgment
    ) {
        emailProcessingService.handle(consumerRecord)
        acknowledgment.acknowledge()
    }

}