package org.abdallah.products.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val tags: List<String> = emptyList(),
    val brand: String? = null,
    val sku: String? = null,
    val weight: Double? = null,
    val dimensions: DimensionsDto? = null,
    val warrantyInformation: String? = null,
    val shippingInformation: String? = null,
    val availabilityStatus: String? = null,
    val reviews: List<ReviewDto> = emptyList(),
    val returnPolicy: String? = null,
    val minimumOrderQuantity: Int? = null,
    val meta: MetaDto? = null,
    val images: List<String> = emptyList(),
    val thumbnail: String
)

@Serializable
data class DimensionsDto(
    val width: Double,
    val height: Double,
    val depth: Double
)

@Serializable
data class ReviewDto(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)

@Serializable
data class MetaDto(
    val createdAt: String,
    val updatedAt: String,
    val barcode: String,
    val qrCode: String
)

