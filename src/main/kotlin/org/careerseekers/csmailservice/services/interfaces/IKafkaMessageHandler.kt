package org.careerseekers.csmailservice.services.interfaces

import org.apache.kafka.clients.consumer.ConsumerRecord

fun interface IKafkaMessageHandler<T, K> {

    fun handle(record: ConsumerRecord<T, K>)
}