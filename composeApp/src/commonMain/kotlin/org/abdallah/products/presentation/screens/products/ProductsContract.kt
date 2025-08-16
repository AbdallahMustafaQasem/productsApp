package org.abdallah.products.presentation.screens.products

import org.abdallah.products.domain.entities.Category
import org.abdallah.products.domain.entities.Product

/**
 * MVI Contract for Products Screen
 */

// User Intents
sealed class ProductsIntent {
    data object LoadProducts : ProductsIntent()
    data object RefreshProducts : ProductsIntent()
    data object LoadNextPage : ProductsIntent()
    
    data class SearchProducts(val query: String) : ProductsIntent()
    data object ClearSearch : ProductsIntent()
    
    data class FilterByCategory(val category: Category?) : ProductsIntent()
    data object LoadCategories : ProductsIntent()
    
    data class SortProducts(val sortBy: String, val order: String) : ProductsIntent()
    
    data class NavigateToProductDetails(val productId: Int) : ProductsIntent()
    
    data object RetryLastAction : ProductsIntent()
}

// UI State
data class ProductsState(
    // Products
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    
    // Pagination
    val currentPage: Int = 0,
    val hasNextPage: Boolean = true,
    val totalProducts: Int = 0,
    
    // Search
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val searchError: String? = null,
    
    // Filters
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val isLoadingCategories: Boolean = false,
    val categoriesError: String? = null,
    
    // Sorting
    val sortBy: String? = null,
    val sortOrder: String? = null,
    
    // UI State
    val isRefreshing: Boolean = false,
    val showEmptyState: Boolean = false
) {
    val hasProducts: Boolean = products.isNotEmpty()
    val isInitialLoading: Boolean = isLoading && products.isEmpty()
    val canLoadMore: Boolean = hasNextPage && !isLoadingMore && !isLoading
}

// Side Effects
sealed class ProductsEffect {
    data class NavigateToProductDetails(val productId: Int) : ProductsEffect()
    data class ShowSnackbar(val message: String) : ProductsEffect()
    data object ScrollToTop : ProductsEffect()
}
