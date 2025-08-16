package org.abdallah.products.domain.entities

/**
 * Product entity for listing (simplified version)
 */
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String?,
    val thumbnail: String,
    val images: List<String>
)

/**
 * Product details entity (complete version)
 */
data class ProductDetails(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val tags: List<String>,
    val brand: String?,
    val sku: String?,
    val weight: Double?,
    val dimensions: Dimensions?,
    val warrantyInformation: String?,
    val shippingInformation: String?,
    val availabilityStatus: String?,
    val reviews: List<Review>,
    val returnPolicy: String?,
    val minimumOrderQuantity: Int?,
    val images: List<String>,
    val thumbnail: String
)

/**
 * Product dimensions
 */
data class Dimensions(
    val width: Double,
    val height: Double,
    val depth: Double
)

/**
 * Product review
 */
data class Review(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)

/**
 * Product list response with pagination
 */
data class ProductListResponse(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
) {
    val hasNextPage: Boolean = skip + limit < total
    val currentPage: Int = if (limit > 0) (skip / limit) + 1 else 1
    val totalPages: Int = if (limit > 0) (total + limit - 1) / limit else 1
}
