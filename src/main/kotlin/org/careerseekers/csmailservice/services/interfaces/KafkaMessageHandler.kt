package org.careerseekers.csmailservice.services.interfaces

import org.apache.kafka.clients.consumer.ConsumerRecord

interface KafkaMessageHandler<T, K> {

    fun handle(record: ConsumerRecord<T, K>)
}