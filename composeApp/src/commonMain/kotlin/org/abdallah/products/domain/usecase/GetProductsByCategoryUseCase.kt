package org.abdallah.products.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.abdallah.products.core.network.NetworkResult
import org.abdallah.products.domain.entities.ProductListResponse
import org.abdallah.products.domain.repository.ProductRepository

class GetProductsByCategoryUseCase(
    private val repository: ProductRepository
) {
    
    suspend operator fun invoke(
        category: String,
        limit: Int = 20,
        skip: Int = 0
    ): NetworkResult<ProductListResponse> {
        if (category.isBlank()) {
            return NetworkResult.Error("Category cannot be empty")
        }
        
        return repository.getProductsByCategory(category.trim(), limit, skip)
    }
}

