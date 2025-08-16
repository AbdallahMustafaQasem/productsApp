package org.abdallah.products.domain.usecase

import kotlinx.coroutines.test.runTest
import org.abdallah.products.core.network.NetworkResult
import org.abdallah.products.domain.entities.ProductListResponse
import org.abdallah.products.domain.repository.ProductRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchProductsUseCaseTest {

    private val mockRepository = MockSearchProductRepository()
    private val useCase = SearchProductsUseCase(mockRepository)

    @Test
    fun `when query is valid, should call repository with trimmed query`() = runTest {
        // Given
        val query = "  phone  "
        val trimmedQuery = "phone"
        val expectedResponse = ProductListResponse(
            products = emptyList(),
            total = 0,
            skip = 0,
            limit = 20
        )
        mockRepository.setSearchProductsResult(NetworkResult.Success(expectedResponse))

        // When
        val result = useCase(query)

        // Then
        assertTrue(result is NetworkResult.Success)
        assertEquals(trimmedQuery, mockRepository.lastSearchQuery)
    }

    @Test
    fun `when query is blank, should return error`() = runTest {
        // Given
        val blankQuery = "   "

        // When
        val result = useCase(blankQuery)

        // Then
        assertTrue(result is NetworkResult.Error)
        assertEquals("Search query cannot be empty", result.message)
    }

    @Test
    fun `when query is empty, should return error`() = runTest {
        // Given
        val emptyQuery = ""

        // When
        val result = useCase(emptyQuery)

        // Then
        assertTrue(result is NetworkResult.Error)
        assertEquals("Search query cannot be empty", result.message)
    }
}

// Mock implementation for testing
private class MockSearchProductRepository : ProductRepository {
    private var searchProductsResult: NetworkResult<ProductListResponse> = 
        NetworkResult.Error("Not set")
    
    var lastSearchQuery: String? = null

    fun setSearchProductsResult(result: NetworkResult<ProductListResponse>) {
        searchProductsResult = result
    }

    override suspend fun searchProducts(
        query: String,
        limit: Int,
        skip: Int
    ): NetworkResult<ProductListResponse> {
        lastSearchQuery = query
        return searchProductsResult
    }

    // Other methods not implemented for this test
    override suspend fun getProducts(
        limit: Int,
        skip: Int,
        sortBy: String?,
        order: String?
    ) = NetworkResult.Error("Not implemented")
    override suspend fun getProductDetails(id: Int) = NetworkResult.Error("Not implemented")
    override suspend fun getCategories() = NetworkResult.Error("Not implemented")
    override suspend fun getProductsByCategory(
        category: String,
        limit: Int,
        skip: Int
    ) = NetworkResult.Error("Not implemented")
    override fun getProductsFlow(limit: Int, skip: Int, sortBy: String?, order: String?) = 
        kotlinx.coroutines.flow.flow { emit(NetworkResult.Error("Not implemented")) }
    override fun searchProductsFlow(query: String, limit: Int, skip: Int) = 
        kotlinx.coroutines.flow.flow { emit(NetworkResult.Error("Not implemented")) }
    override suspend fun clearCache() {}
    override suspend fun refreshProducts() = NetworkResult.Error("Not implemented")
}

