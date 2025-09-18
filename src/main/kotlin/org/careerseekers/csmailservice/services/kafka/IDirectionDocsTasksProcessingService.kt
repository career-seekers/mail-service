package org.careerseekers.csmailservice.services.kafka

import org.careerseekers.csmailservice.dto.DirectionDocumentsTask
import org.careerseekers.csmailservice.enums.DirectionDocsEventTypes

interface IDirectionDocsTasksProcessingService : KafkaMessageHandler<String, DirectionDocumentsTask> {
    val eventType: DirectionDocsEventTypes
}