package org.careerseekers.csmailservice.cache

import com.careerseekers.grpc.users.UserId
import com.careerseekers.grpc.users.UsersServiceGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.careerseekers.csmailservice.dto.UsersCacheDto
import org.careerseekers.csmailservice.io.converters.extensions.toCache
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

    @GrpcClient("users-service")
    lateinit var usersServiceStub: UsersServiceGrpc.UsersServiceBlockingStub

    override fun getItemFromCache(key: Any): UsersCacheDto? {
        val user = cache?.get(key)?.let {
            it.get() as? UsersCacheDto
        } ?: usersServiceStub.getById(
            UserId.newBuilder()
                .setId(key as Long)
                .build()
        ).toCache()

        return user
    }
}