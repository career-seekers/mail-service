package org.careerseekers.csmailservice.cache

import com.careerseekers.grpc.users.UserId
import com.careerseekers.grpc.users.UsersServiceGrpc
import io.grpc.StatusRuntimeException
import net.devh.boot.grpc.client.inject.GrpcClient
import org.careerseekers.csmailservice.dto.UsersCacheDto
import org.careerseekers.csmailservice.exceptions.GrpcServiceUnavailableException
import org.careerseekers.csmailservice.io.converters.extensions.toCache
import org.springframework.cache.CacheManager
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class UsersCacheClient(
    override val redisTemplate: RedisTemplate<String, UsersCacheDto>,
    cacheManager: CacheManager,
) : CacheRetriever<UsersCacheDto> {
    override val cacheKey = "users"
    private val cache = cacheManager.getCache(cacheKey)

    @GrpcClient("users-service")
    lateinit var usersServiceStub: UsersServiceGrpc.UsersServiceBlockingStub

    override fun getItemFromCache(key: Any): UsersCacheDto? {
        val user = cache?.get(key)?.let {
            it.get() as? UsersCacheDto
        } ?: getUser(key as Long)

        return user
    }

    private fun getUser(id: Long): UsersCacheDto {
        try {
            return usersServiceStub.getById(
                UserId.newBuilder()
                    .setId(id)
                    .build()
            ).toCache()
        } catch (_: StatusRuntimeException) {
            throw GrpcServiceUnavailableException("Users gRPC service unavailable now.")
        }
    }
}