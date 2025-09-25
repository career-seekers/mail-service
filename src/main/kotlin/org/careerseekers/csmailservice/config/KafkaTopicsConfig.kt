package org.careerseekers.csmailservice.config

import org.apache.kafka.clients.admin.NewTopic
import org.careerseekers.csmailservice.enums.KafkaTopics
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaTopicsConfig {

    @Bean
    fun emailSendingTasksTopic(): NewTopic {
        return TopicBuilder
            .name(KafkaTopics.EMAIL_SENDING_TASKS.name)
            .partitions(12)
            .replicas(3)
            .build()
    }

    @Bean
    fun platformCreationTopic(): NewTopic {
        return TopicBuilder
            .name(KafkaTopics.PLATFORM_CREATION.name)
            .partitions(12)
            .replicas(3)
            .build()
    }

    @Bean
    fun tgLinksNotificationTopic(): NewTopic {
        return TopicBuilder
            .name(KafkaTopics.TG_LINKS_NOTIFICATION.name)
            .partitions(12)
            .replicas(3)
            .build()
    }

    @Bean
    fun directionCreationTopic(): NewTopic {
        return TopicBuilder
            .name(KafkaTopics.DIRECTION_CREATION.name)
            .partitions(12)
            .replicas(3)
            .build()
    }

    @Bean
    fun directionDocumentsTasksTopic(): NewTopic {
        return TopicBuilder
            .name(KafkaTopics.DIRECTION_DOCUMENTS_TASKS.name)
            .partitions(12)
            .replicas(3)
            .build()
    }

    @Bean
    fun universalEmailMessagesTopic(): NewTopic {
        return TopicBuilder
            .name(KafkaTopics.UNIVERSAL_EMAIL_MESSAGES_TOPIC.name)
            .partitions(12)
            .replicas(3)
            .build()
    }
}