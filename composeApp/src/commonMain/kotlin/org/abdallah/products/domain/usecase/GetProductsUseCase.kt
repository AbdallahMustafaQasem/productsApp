package org.abdallah.products.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.abdallah.products.core.network.NetworkResult
import org.abdallah.products.domain.entities.ProductListResponse
import org.abdallah.products.domain.repository.ProductRepository

class GetProductsUseCase(
    private val repository: ProductRepository
) {
    
    suspend operator fun invoke(
        limit: Int = 20,
        skip: Int = 0,
        sortBy: String? = null,
        order: String? = null
    ): NetworkResult<ProductListResponse> {
        return repository.getProducts(limit, skip, sortBy, order)
    }
    
    fun asFlow(
        limit: Int = 20,
        skip: Int = 0,
        sortBy: String? = null,
        order: String? = null
    ): Flow<NetworkResult<ProductListResponse>> {
        return repository.getProductsFlow(limit, skip, sortBy, order)
    }
}

