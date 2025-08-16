package org.abdallah.products.data.remote.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.abdallah.products.core.constants.ApiConstants
import org.abdallah.products.data.remote.dto.CategoryDto
import org.abdallah.products.data.remote.dto.ProductDto
import org.abdallah.products.data.remote.dto.ProductListResponseDto

interface ProductsApi {
    
    /**
     * Get products with pagination and optional sorting
     */
    suspend fun getProducts(
        limit: Int = ApiConstants.DEFAULT_PAGE_SIZE,
        skip: Int = 0,
        sortBy: String? = null,
        order: String? = null
    ): ProductListResponseDto
    
    /**
     * Search products by query
     */
    suspend fun searchProducts(
        query: String,
        limit: Int = ApiConstants.DEFAULT_PAGE_SIZE,
        skip: Int = 0
    ): ProductListResponseDto
    
    /**
     * Get single product by ID
     */
    suspend fun getProduct(id: Int): ProductDto
    
    /**
     * Get all categories
     */
    suspend fun getCategories(): List<CategoryDto>
    
    /**
     * Get products by category
     */
    suspend fun getProductsByCategory(
        category: String,
        limit: Int = ApiConstants.DEFAULT_PAGE_SIZE,
        skip: Int = 0
    ): ProductListResponseDto
}

class ProductsApiImpl(
    private val httpClient: HttpClient
) : ProductsApi {
    
    override suspend fun getProducts(
        limit: Int,
        skip: Int,
        sortBy: String?,
        order: String?
    ): ProductListResponseDto {
        val response: HttpResponse = httpClient.get(ApiConstants.PRODUCTS) {
            parameter(ApiConstants.LIMIT, limit)
            parameter(ApiConstants.SKIP, skip)
            sortBy?.let { parameter(ApiConstants.SORT_BY, it) }
            order?.let { parameter(ApiConstants.ORDER, it) }
        }
        return response.body()
    }
    
    override suspend fun searchProducts(
        query: String,
        limit: Int,
        skip: Int
    ): ProductListResponseDto {
        val response: HttpResponse = httpClient.get(ApiConstants.PRODUCTS_SEARCH) {
            parameter(ApiConstants.QUERY, query)
            parameter(ApiConstants.LIMIT, limit)
            parameter(ApiConstants.SKIP, skip)
        }
        return response.body()
    }
    
    override suspend fun getProduct(id: Int): ProductDto {
        val response: HttpResponse = httpClient.get("${ApiConstants.PRODUCTS}/$id")
        return response.body()
    }
    
    override suspend fun getCategories(): List<CategoryDto> {
        val response: HttpResponse = httpClient.get(ApiConstants.CATEGORIES)
        return response.body()
    }
    
    override suspend fun getProductsByCategory(
        category: String,
        limit: Int,
        skip: Int
    ): ProductListResponseDto {
        val response: HttpResponse = httpClient.get("${ApiConstants.PRODUCTS}/category/$category") {
            parameter(ApiConstants.LIMIT, limit)
            parameter(ApiConstants.SKIP, skip)
        }
        return response.body()
    }
}
