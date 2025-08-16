package org.abdallah.products.domain.usecase

import org.abdallah.products.core.network.NetworkResult
import org.abdallah.products.domain.entities.Category
import org.abdallah.products.domain.repository.ProductRepository

class GetCategoriesUseCase(
    private val repository: ProductRepository
) {
    
    suspend operator fun invoke(): NetworkResult<List<Category>> {
        return repository.getCategories()
    }
}

