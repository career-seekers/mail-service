package org.careerseekers.csmailservice.cache

import org.careerseekers.csmailservice.dto.UsersCacheDto
import org.springframework.cache.CacheManager
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class UsersCacheClient(
    override val redisTemplate: RedisTemplate<String, UsersCacheDto>,
    cacheManager: CacheManager,
) : CacheClient<UsersCacheDto> {
    override val cacheKey = "users"
    private val cache = cacheManager.getCache(cacheKey)

    override fun getItemFromCache(key: Any): UsersCacheDto? {
        return cache?.get(key)?.let { it.get() as? UsersCacheDto }
    }
}