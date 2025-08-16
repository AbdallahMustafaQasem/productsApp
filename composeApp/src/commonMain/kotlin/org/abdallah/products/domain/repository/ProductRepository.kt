package org.abdallah.products.domain.repository

import kotlinx.coroutines.flow.Flow
import org.abdallah.products.core.network.NetworkResult
import org.abdallah.products.domain.entities.*

interface ProductRepository {
    
    /**
     * Get products with pagination and optional sorting
     */
    suspend fun getProducts(
        limit: Int = 20,
        skip: Int = 0,
        sortBy: String? = null,
        order: String? = null
    ): NetworkResult<ProductListResponse>
    
    /**
     * Search products by query with pagination
     */
    suspend fun searchProducts(
        query: String,
        limit: Int = 20,
        skip: Int = 0
    ): NetworkResult<ProductListResponse>
    
    /**
     * Get single product details by ID
     */
    suspend fun getProductDetails(id: Int): NetworkResult<ProductDetails>
    
    /**
     * Get all available categories
     */
    suspend fun getCategories(): NetworkResult<List<Category>>
    
    /**
     * Get products by specific category with pagination
     */
    suspend fun getProductsByCategory(
        category: String,
        limit: Int = 20,
        skip: Int = 0
    ): NetworkResult<ProductListResponse>
    
    /**
     * Get products as Flow for reactive updates
     */
    fun getProductsFlow(
        limit: Int = 20,
        skip: Int = 0,
        sortBy: String? = null,
        order: String? = null
    ): Flow<NetworkResult<ProductListResponse>>
    
    /**
     * Search products as Flow for reactive updates
     */
    fun searchProductsFlow(
        query: String,
        limit: Int = 20,
        skip: Int = 0
    ): Flow<NetworkResult<ProductListResponse>>
    
    /**
     * Clear cache (if any)
     */
    suspend fun clearCache()
    
    /**
     * Refresh products (clear cache and fetch new data)
     */
    suspend fun refreshProducts(): NetworkResult<ProductListResponse>
}

