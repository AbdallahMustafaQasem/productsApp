package org.abdallah.products.data.cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Simple in-memory cache implementation
 */
class InMemoryCache<K, V> {
    
    private val cache = mutableMapOf<K, CacheEntry<V>>()
    private val mutex = Mutex()
    
    data class CacheEntry<V>(
        val value: V,
        val timestamp: Long,
        val ttl: Long = DEFAULT_TTL
    ) {
        val isExpired: Boolean
            get() = System.currentTimeMillis() - timestamp > ttl
    }
    
    suspend fun get(key: K): V? = mutex.withLock {
        val entry = cache[key]
        return if (entry != null && !entry.isExpired) {
            entry.value
        } else {
            cache.remove(key)
            null
        }
    }
    
    suspend fun put(key: K, value: V, ttl: Long = DEFAULT_TTL) = mutex.withLock {
        cache[key] = CacheEntry(value, System.currentTimeMillis(), ttl)
    }
    
    suspend fun remove(key: K) = mutex.withLock {
        cache.remove(key)
    }
    
    suspend fun clear() = mutex.withLock {
        cache.clear()
    }
    
    suspend fun size(): Int = mutex.withLock {
        cache.size
    }
    
    companion object {
        const val DEFAULT_TTL = 5 * 60 * 1000L // 5 minutes
        const val LONG_TTL = 30 * 60 * 1000L // 30 minutes
    }
}

