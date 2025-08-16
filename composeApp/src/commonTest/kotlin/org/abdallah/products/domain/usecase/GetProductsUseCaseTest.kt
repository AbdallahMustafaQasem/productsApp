package org.abdallah.products.domain.usecase

import kotlinx.coroutines.test.runTest
import org.abdallah.products.core.network.NetworkResult
import org.abdallah.products.domain.entities.Product
import org.abdallah.products.domain.entities.ProductListResponse
import org.abdallah.products.domain.repository.ProductRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class GetProductsUseCaseTest {

    private val mockRepository = MockProductRepository()
    private val useCase = GetProductsUseCase(mockRepository)

    @Test
    fun `when repository returns success, useCase should return success`() = runTest {
        // Given
        val expectedProducts = listOf(
            Product(
                id = 1,
                title = "Test Product",
                description = "Test Description",
                category = "electronics",
                price = 99.99,
                discountPercentage = 10.0,
                rating = 4.5,
                stock = 50,
                brand = "Test Brand",
                thumbnail = "test.jpg",
                images = listOf("test1.jpg", "test2.jpg")
            )
        )
        val expectedResponse = ProductListResponse(
            products = expectedProducts,
            total = 1,
            skip = 0,
            limit = 20
        )
        mockRepository.setGetProductsResult(NetworkResult.Success(expectedResponse))

        // When
        val result = useCase()

        // Then
        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResponse, result.data)
    }

    @Test
    fun `when repository returns error, useCase should return error`() = runTest {
        // Given
        val errorMessage = "Network error"
        mockRepository.setGetProductsResult(NetworkResult.Error(errorMessage))

        // When
        val result = useCase()

        // Then
        assertTrue(result is NetworkResult.Error)
        assertEquals(errorMessage, result.message)
    }
}

// Mock implementation for testing
private class MockProductRepository : ProductRepository {
    private var getProductsResult: NetworkResult<ProductListResponse> = 
        NetworkResult.Error("Not set")

    fun setGetProductsResult(result: NetworkResult<ProductListResponse>) {
        getProductsResult = result
    }

    override suspend fun getProducts(
        limit: Int,
        skip: Int,
        sortBy: String?,
        order: String?
    ): NetworkResult<ProductListResponse> = getProductsResult

    // Other methods not implemented for this test
    override suspend fun searchProducts(
        query: String,
        limit: Int,
        skip: Int
    ): NetworkResult<ProductListResponse> = NetworkResult.Error("Not implemented")

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

