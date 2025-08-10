package org.careerseekers.csmailservice.cache

interface CacheLoader<T> : CacheClient<T> {
    fun preloadCache(): Any
    fun loadItemToCache(item: T): Any
}