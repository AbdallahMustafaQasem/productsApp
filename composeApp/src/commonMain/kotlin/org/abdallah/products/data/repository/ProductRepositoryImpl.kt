package org.abdallah.products.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.abdallah.products.core.network.NetworkResult
import org.abdallah.products.core.network.map
import org.abdallah.products.core.utils.safeCall
import org.abdallah.products.data.cache.InMemoryCache
import org.abdallah.products.data.mapper.*
import org.abdallah.products.data.remote.api.ProductsApi
import org.abdallah.products.data.remote.dto.CategoryDto
import org.abdallah.products.data.remote.dto.ProductDto
import org.abdallah.products.data.remote.dto.ProductListResponseDto
import org.abdallah.products.domain.entities.*
import org.abdallah.products.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val api: ProductsApi
) : ProductRepository {
    
    // Caches
    private val productsCache = InMemoryCache<String, ProductListResponse>()
    private val productDetailsCache = InMemoryCache<Int, ProductDetails>()
    private val categoriesCache = InMemoryCache<String, List<Category>>()
    
    override suspend fun getProducts(
        limit: Int,
        skip: Int,
        sortBy: String?,
        order: String?
    ): NetworkResult<ProductListResponse> {
        val cacheKey = "products_${limit}_${skip}_${sortBy}_${order}"
        
        // Try cache first
        productsCache.get(cacheKey)?.let {
            return NetworkResult.Success(it)
        }
        
        // Make API call
        return safeCall {
            api.getProducts(limit, skip, sortBy, order)
        }.map { dto ->
            val domainResponse = dto.toDomain()
            // Cache the result
            productsCache.put(cacheKey, domainResponse)
            domainResponse
        }
    }
    
    override suspend fun searchProducts(
        query: String,
        limit: Int,
        skip: Int
    ): NetworkResult<ProductListResponse> {
        val cacheKey = "search_${query}_${limit}_${skip}"
        
        // Try cache first
        productsCache.get(cacheKey)?.let {
            return NetworkResult.Success(it)
        }
        
        // Make API call
        return safeCall {
            api.searchProducts(query, limit, skip)
        }.map { dto ->
            val domainResponse = dto.toDomain()
            // Cache the result
            productsCache.put(cacheKey, domainResponse)
            domainResponse
        }
    }
    
    override suspend fun getProductDetails(id: Int): NetworkResult<ProductDetails> {
        // Try cache first
        productDetailsCache.get(id)?.let {
            return NetworkResult.Success(it)
        }
        
        // Make API call
        return safeCall {
            api.getProduct(id)
        }.map { dto ->
            val domainProduct = dto.toProductDetails()
            // Cache the result
            productDetailsCache.put(id, domainProduct)
            domainProduct
        }
    }
    
    override suspend fun getCategories(): NetworkResult<List<Category>> {
        val cacheKey = "categories"
        
        // Try cache first
        categoriesCache.get(cacheKey)?.let {
            return NetworkResult.Success(it)
        }
        
        // Make API call
        return safeCall {
            api.getCategories()
        }.map { dtoList ->
            val domainCategories = dtoList.map { it.toDomain() }
            // Cache the result with longer TTL
            categoriesCache.put(cacheKey, domainCategories, InMemoryCache.LONG_TTL)
            domainCategories
        }
    }
    
    override suspend fun getProductsByCategory(
        category: String,
        limit: Int,
        skip: Int
    ): NetworkResult<ProductListResponse> {
        val cacheKey = "category_${category}_${limit}_${skip}"
        
        // Try cache first
        productsCache.get(cacheKey)?.let {
            return NetworkResult.Success(it)
        }
        
        // Make API call
        return safeCall {
            api.getProductsByCategory(category, limit, skip)
        }.map { dto ->
            val domainResponse = dto.toDomain()
            // Cache the result
            productsCache.put(cacheKey, domainResponse)
            domainResponse
        }
    }
    
    override fun getProductsFlow(
        limit: Int,
        skip: Int,
        sortBy: String?,
        order: String?
    ): Flow<NetworkResult<ProductListResponse>> = flow {
        emit(NetworkResult.Loading)
        val result = getProducts(limit, skip, sortBy, order)
        emit(result)
    }
    
    override fun searchProductsFlow(
        query: String,
        limit: Int,
        skip: Int
    ): Flow<NetworkResult<ProductListResponse>> = flow {
        emit(NetworkResult.Loading)
        val result = searchProducts(query, limit, skip)
        emit(result)
    }
    
    override suspend fun clearCache() {
        productsCache.clear()
        productDetailsCache.clear()
        categoriesCache.clear()
    }
    
    override suspend fun refreshProducts(): NetworkResult<ProductListResponse> {
        // Clear cache and fetch fresh data
        productsCache.clear()
        return getProducts()
    }
}
