package org.abdallah.products.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Category DTO - represents category data from API
 * Note: The API returns categories as simple strings in an array
 */
typealias CategoryDto = String

/**
 * For more complex category data (if API changes in future)
 */
@Serializable
data class CategoryDetailDto(
    val slug: String,
    val name: String,
    val url: String
)

