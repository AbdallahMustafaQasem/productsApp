package org.abdallah.products.data.mapper

import org.abdallah.products.data.remote.dto.*
import org.abdallah.products.domain.entities.*

/**
 * Extension functions to map DTOs to Domain entities
 */

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        thumbnail = thumbnail,
        images = images
    )
}

fun ProductDto.toProductDetails(): ProductDetails {
    return ProductDetails(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        tags = tags,
        brand = brand,
        sku = sku,
        weight = weight,
        dimensions = dimensions?.toDomain(),
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        reviews = reviews.map { it.toDomain() },
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        images = images,
        thumbnail = thumbnail
    )
}

fun DimensionsDto.toDomain(): Dimensions {
    return Dimensions(
        width = width,
        height = height,
        depth = depth
    )
}

fun ReviewDto.toDomain(): Review {
    return Review(
        rating = rating,
        comment = comment,
        date = date,
        reviewerName = reviewerName,
        reviewerEmail = reviewerEmail
    )
}

fun ProductListResponseDto.toDomain(): ProductListResponse {
    return ProductListResponse(
        products = products.map { it.toDomain() },
        total = total,
        skip = skip,
        limit = limit
    )
}

fun CategoryDto.toDomain(): Category {
    return Category(
        name = this,
        slug = this.lowercase().replace(" ", "-")
    )
}

