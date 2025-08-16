package org.abdallah.products.domain.usecase

import org.abdallah.products.core.network.NetworkResult
import org.abdallah.products.domain.entities.ProductDetails
import org.abdallah.products.domain.repository.ProductRepository

class GetProductDetailsUseCase(
    private val repository: ProductRepository
) {
    
    suspend operator fun invoke(productId: Int): NetworkResult<ProductDetails> {
        if (productId <= 0) {
            return NetworkResult.Error("Invalid product ID")
        }
        
        return repository.getProductDetails(productId)
    }
}

