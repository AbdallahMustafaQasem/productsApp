package org.abdallah.products.core.utils

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.abdallah.products.core.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Extension function to safely make API calls and handle errors
 */
suspend inline fun <reified T> safeApiCall(
    apiCall: suspend () -> HttpResponse
): NetworkResult<T> {
    return try {
        val response = apiCall()
        if (response.status.isSuccess()) {
            val data = response.body<T>()
            NetworkResult.Success(data)
        } else {
            NetworkResult.Error(
                message = "API call failed with status: ${response.status}",
                code = response.status.value
            )
        }
    } catch (e: Exception) {
        NetworkResult.Error(
            message = e.message ?: "Unknown error occurred",
            exception = e
        )
    }
}

/**
 * Extension function to safely execute API calls that return data directly
 */
suspend inline fun <T> safeCall(
    apiCall: suspend () -> T
): NetworkResult<T> {
    return try {
        val data = apiCall()
        NetworkResult.Success(data)
    } catch (e: Exception) {
        NetworkResult.Error(
            message = e.message ?: "Unknown error occurred",
            exception = e
        )
    }
}

/**
 * Extension function to convert Flow to NetworkResult Flow
 */
fun <T> Flow<T>.asNetworkResult(): Flow<NetworkResult<T>> {
    return this
        .map<T, NetworkResult<T>> { NetworkResult.Success(it) }
        .onStart { emit(NetworkResult.Loading) }
        .catch { emit(NetworkResult.Error(it.message ?: "Unknown error", exception = it)) }
}

/**
 * Format price with currency
 */
fun Double.formatPrice(): String {
    return "$%.2f".format(this)
}

/**
 * Format rating with star
 */
fun Double.formatRating(): String {
    return "★ %.1f".format(this)
}

/**
 * Capitalize first letter of each word
 */
fun String.toTitleCase(): String {
    return this.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

/**
 * Truncate text to specified length
 */
fun String.truncate(maxLength: Int): String {
    return if (this.length <= maxLength) this else "${this.take(maxLength)}..."
}
