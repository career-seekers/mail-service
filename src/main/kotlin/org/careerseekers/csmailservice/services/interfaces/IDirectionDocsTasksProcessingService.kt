package org.careerseekers.csmailservice.services.interfaces

import org.careerseekers.csmailservice.dto.DirectionDocumentsTask
import org.careerseekers.csmailservice.enums.DirectionDocsEventTypes

interface IDirectionDocsTasksProcessingService : KafkaMessageHandler<String, DirectionDocumentsTask> {
    val eventType: DirectionDocsEventTypes
}