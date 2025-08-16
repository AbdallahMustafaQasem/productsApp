package org.abdallah.products.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductListResponseDto(
    val products: List<ProductDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

