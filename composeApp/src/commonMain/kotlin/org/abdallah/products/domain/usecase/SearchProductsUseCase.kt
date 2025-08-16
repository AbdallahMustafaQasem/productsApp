package org.abdallah.products.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.abdallah.products.core.network.NetworkResult
import org.abdallah.products.domain.entities.ProductListResponse
import org.abdallah.products.domain.repository.ProductRepository

class SearchProductsUseCase(
    private val repository: ProductRepository
) {
    
    suspend operator fun invoke(
        query: String,
        limit: Int = 20,
        skip: Int = 0
    ): NetworkResult<ProductListResponse> {
        if (query.isBlank()) {
            return NetworkResult.Error("Search query cannot be empty")
        }
        
        return repository.searchProducts(query.trim(), limit, skip)
    }
    
    fun asFlow(
        query: String,
        limit: Int = 20,
        skip: Int = 0
    ): Flow<NetworkResult<ProductListResponse>> {
        return repository.searchProductsFlow(query.trim(), limit, skip)
    }
}

