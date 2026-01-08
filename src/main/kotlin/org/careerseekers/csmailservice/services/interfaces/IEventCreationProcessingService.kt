package org.careerseekers.csmailservice.services.interfaces

import org.careerseekers.csmailservice.dto.EventCreationDto
import org.careerseekers.csmailservice.enums.EventTypes

interface IEventCreationProcessingService : IKafkaMessageHandler<String, EventCreationDto> {
    val eventType: EventTypes
}