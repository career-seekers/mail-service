package org.careerseekers.csmailservice.services.kafka.consumers

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csmailservice.dto.EmailSendingTaskDto
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class KafkaEmailSendingConsumer : CustomKafkaConsumer<String, EmailSendingTaskDto> {

    @KafkaListener(
        topics = ["EMAIL_SENDING_TASKS"],
        groupId = "email_sending_tasks_consumer"
    )
    override fun receiveMessage(
        consumerRecord: ConsumerRecord<String, EmailSendingTaskDto>,
        acknowledgment: Acknowledgment
    ) {
        println(consumerRecord.value())

        acknowledgment.acknowledge()
    }

}