package org.careerseekers.csmailservice.config

import org.careerseekers.csmailservice.dto.CachesDto
import org.careerseekers.csmailservice.dto.UsersCacheDto
import org.careerseekers.csmailservice.serializers.PolymorphicRedisSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisTemplatesConfig(
    private val serializer: PolymorphicRedisSerializer<out CachesDto>
) {

    @Bean
    @Qualifier("users")
    fun usersRedisTemplate(
        connectionFactory: RedisConnectionFactory
    ): RedisTemplate<String, UsersCacheDto> {
        val template = RedisTemplate<String, UsersCacheDto>()
        template.connectionFactory = connectionFactory

        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = serializer

        template.hashKeySerializer = StringRedisSerializer()
        template.hashValueSerializer = serializer

        template.afterPropertiesSet()
        return template
    }
}