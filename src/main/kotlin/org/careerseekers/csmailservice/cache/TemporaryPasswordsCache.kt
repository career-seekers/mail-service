package org.careerseekers.csmailservice.cache

import org.careerseekers.csmailservice.dto.TemporaryPasswordDto
import org.springframework.cache.CacheManager
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class TemporaryPasswordsCache(
    override val redisTemplate: RedisTemplate<String, TemporaryPasswordDto>,
    cacheManager: CacheManager,
) : CacheRetriever<TemporaryPasswordDto> {
    override val cacheKey = "temporaryPasswords"
    private val cache = cacheManager.getCache(cacheKey)

    override fun getItemFromCache(key: Any): TemporaryPasswordDto? {
        val password = cache?.get(key)?.let {
            it.get() as? TemporaryPasswordDto
        }

        return password
    }
}