package org.careerseekers.csmailservice.config

import org.careerseekers.csmailservice.dto.CachesDto
import org.careerseekers.csmailservice.dto.json
import org.careerseekers.csmailservice.serializers.PolymorphicRedisSerializer
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration

@Configuration
class CacheConfig {

    @Bean
    fun cacheConfiguration30min(): RedisCacheConfiguration {
        val serializer = PolymorphicRedisSerializer(CachesDto.serializer(), json)

        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(30))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
            .disableCachingNullValues()
    }

    @Bean
    fun cacheConfiguration5min(): RedisCacheConfiguration {
        return cacheConfiguration30min().entryTtl(Duration.ofMinutes(5))
    }

    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory): CacheManager {
        return RedisCacheManager.builder(connectionFactory)
            .withCacheConfiguration("verification_code", cacheConfiguration5min())
            .cacheDefaults(cacheConfiguration30min())
            .build()
    }
}