package org.abdallah.products.core.network

/**
 * A sealed class representing the result of a network operation
 */
sealed class NetworkResult<out T> {
    
    /**
     * Successful network operation with data
     */
    data class Success<T>(val data: T) : NetworkResult<T>()
    
    /**
     * Network operation failed with an error
     */
    data class Error(
        val message: String,
        val code: Int? = null,
        val exception: Throwable? = null
    ) : NetworkResult<Nothing>()
    
    /**
     * Network operation is in progress
     */
    data object Loading : NetworkResult<Nothing>()
}

/**
 * Extension functions for NetworkResult
 */
inline fun <T> NetworkResult<T>.onSuccess(action: (T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) action(data)
    return this
}

inline fun <T> NetworkResult<T>.onError(action: (String, Int?, Throwable?) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Error) action(message, code, exception)
    return this
}

inline fun <T> NetworkResult<T>.onLoading(action: () -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Loading) action()
    return this
}

/**
 * Map the success value to another type
 */
inline fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.Success(transform(data))
        is NetworkResult.Error -> this
        is NetworkResult.Loading -> this
    }
}

/**
 * Get data or null
 */
fun <T> NetworkResult<T>.getOrNull(): T? {
    return when (this) {
        is NetworkResult.Success -> data
        else -> null
    }
}

