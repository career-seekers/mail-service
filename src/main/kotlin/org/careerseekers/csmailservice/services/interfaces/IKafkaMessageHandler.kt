package org.careerseekers.csmailservice.services.interfaces

import org.apache.kafka.clients.consumer.ConsumerRecord

interface IKafkaMessageHandler<T, K> {

    fun handle(record: ConsumerRecord<T, K>)
}