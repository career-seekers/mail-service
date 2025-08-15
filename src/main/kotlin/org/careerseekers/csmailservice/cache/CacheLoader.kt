package org.careerseekers.csmailservice.cache

interface CacheLoader<T> : CacheClient<T> {
    fun loadItemToCache(item: T): Any
}