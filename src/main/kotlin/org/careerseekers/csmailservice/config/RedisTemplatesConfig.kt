package org.careerseekers.csmailservice.config

import org.careerseekers.csmailservice.dto.CachesDto
import org.careerseekers.csmailservice.dto.UsersCacheDto
import org.careerseekers.csmailservice.dto.json
import org.careerseekers.csmailservice.serializers.PolymorphicRedisSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisTemplatesConfig {

    @Bean
    @Qualifier("users")
    fun usersRedisTemplate(
        connectionFactory: RedisConnectionFactory
    ): RedisTemplate<String, UsersCacheDto> {
        val template = RedisTemplate<String, UsersCacheDto>()
        template.connectionFactory = connectionFactory

        val serializer = PolymorphicRedisSerializer(CachesDto.serializer(), json)

        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = serializer

        template.hashKeySerializer = StringRedisSerializer()
        template.hashValueSerializer = serializer

        template.afterPropertiesSet()
        return template
    }
}