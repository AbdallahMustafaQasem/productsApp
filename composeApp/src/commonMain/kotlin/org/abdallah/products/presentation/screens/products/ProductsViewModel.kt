package org.abdallah.products.presentation.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.abdallah.products.core.network.NetworkResult
import org.abdallah.products.domain.entities.Category
import org.abdallah.products.domain.usecase.*

class ProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductsState())
    val state: StateFlow<ProductsState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<ProductsEffect>()
    val effects: SharedFlow<ProductsEffect> = _effects.asSharedFlow()

    // Constants
    private val pageSize = 20

    init {
        handleIntent(ProductsIntent.LoadProducts)
        handleIntent(ProductsIntent.LoadCategories)
    }

    fun handleIntent(intent: ProductsIntent) {
        when (intent) {
            is ProductsIntent.LoadProducts -> loadProducts()
            is ProductsIntent.RefreshProducts -> refreshProducts()
            is ProductsIntent.LoadNextPage -> loadNextPage()
            
            is ProductsIntent.SearchProducts -> searchProducts(intent.query)
            is ProductsIntent.ClearSearch -> clearSearch()
            
            is ProductsIntent.FilterByCategory -> filterByCategory(intent.category)
            is ProductsIntent.LoadCategories -> loadCategories()
            
            is ProductsIntent.SortProducts -> sortProducts(intent.sortBy, intent.order)
            
            is ProductsIntent.NavigateToProductDetails -> navigateToProductDetails(intent.productId)
            
            is ProductsIntent.RetryLastAction -> retryLastAction()
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            val result = getProductsUseCase(pageSize, 0, null, null)

            when (result) {
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(
                        products = result.data.products,
                        isLoading = false,
                        currentPage = 1,
                        hasNextPage = result.data.hasNextPage,
                        totalProducts = result.data.total,
                        showEmptyState = result.data.products.isEmpty()
                    )
                }
                is NetworkResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun refreshProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true)

            val currentState = _state.value
            val result = when {
                currentState.searchQuery.isNotEmpty() -> 
                    searchProductsUseCase(currentState.searchQuery, pageSize, 0)
                currentState.selectedCategory != null -> 
                    getProductsByCategoryUseCase(currentState.selectedCategory.slug, pageSize, 0)
                else -> 
                    getProductsUseCase(pageSize, 0, currentState.sortBy, currentState.sortOrder)
            }

            when (result) {
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(
                        products = result.data.products,
                        isRefreshing = false,
                        currentPage = 1,
                        hasNextPage = result.data.hasNextPage,
                        totalProducts = result.data.total,
                        showEmptyState = result.data.products.isEmpty()
                    )
                    _effects.emit(ProductsEffect.ScrollToTop)
                }
                is NetworkResult.Error -> {
                    _state.value = _state.value.copy(
                        isRefreshing = false,
                        error = result.message
                    )
                    _effects.emit(ProductsEffect.ShowSnackbar("Failed to refresh products"))
                }
                is NetworkResult.Loading -> {
                    _state.value = _state.value.copy(isRefreshing = true)
                }
            }
        }
    }

    private fun loadNextPage() {
        if (!_state.value.canLoadMore) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoadingMore = true)

            val currentState = _state.value
            val skip = currentState.currentPage * pageSize

            val result = if (currentState.searchQuery.isNotEmpty()) {
                searchProductsUseCase(currentState.searchQuery, pageSize, skip)
            } else if (currentState.selectedCategory != null) {
                getProductsByCategoryUseCase(currentState.selectedCategory.slug, pageSize, skip)
            } else {
                getProductsUseCase(pageSize, skip, currentState.sortBy, currentState.sortOrder)
            }

            when (result) {
                is NetworkResult.Success -> {
                    val existingIds = _state.value.products.map { it.id }.toSet()
                    val newProducts = result.data.products.filter { it.id !in existingIds }
                    _state.value = _state.value.copy(
                        products = _state.value.products + newProducts,
                        isLoadingMore = false,
                        currentPage = _state.value.currentPage + 1,
                        hasNextPage = result.data.hasNextPage
                    )
                }
                is NetworkResult.Error -> {
                    _state.value = _state.value.copy(isLoadingMore = false)
                    _effects.emit(ProductsEffect.ShowSnackbar("Failed to load more products"))
                }
                is NetworkResult.Loading -> {
                    _state.value = _state.value.copy(isLoadingMore = true)
                }
            }
        }
    }

    private fun searchProducts(query: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                searchQuery = query,
                isSearching = true,
                searchError = null,
                selectedCategory = null // Clear category filter when searching
            )

            if (query.isBlank()) {
                clearSearch()
                return@launch
            }

            val result = searchProductsUseCase(query, pageSize, 0)

            when (result) {
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(
                        products = result.data.products,
                        isSearching = false,
                        currentPage = 1,
                        hasNextPage = result.data.hasNextPage,
                        totalProducts = result.data.total,
                        showEmptyState = result.data.products.isEmpty()
                    )
                }
                is NetworkResult.Error -> {
                    _state.value = _state.value.copy(
                        isSearching = false,
                        searchError = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    _state.value = _state.value.copy(isSearching = true)
                }
            }
        }
    }

    private fun clearSearch() {
        _state.value = _state.value.copy(
            searchQuery = "",
            isSearching = false,
            searchError = null
        )
        loadProducts()
    }

    private fun filterByCategory(category: Category?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                selectedCategory = category,
                isLoading = true,
                searchQuery = "", // Clear search when filtering by category
                error = null
            )

            val result = if (category != null) {
                getProductsByCategoryUseCase(category.slug, pageSize, 0)
            } else {
                getProductsUseCase(pageSize, 0, _state.value.sortBy, _state.value.sortOrder)
            }

            when (result) {
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(
                        products = result.data.products,
                        isLoading = false,
                        currentPage = 1,
                        hasNextPage = result.data.hasNextPage,
                        totalProducts = result.data.total,
                        showEmptyState = result.data.products.isEmpty()
                    )
                    _effects.emit(ProductsEffect.ScrollToTop)
                }
                is NetworkResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoadingCategories = true)

            when (val result = getCategoriesUseCase()) {
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(
                        categories = result.data,
                        isLoadingCategories = false,
                        categoriesError = null
                    )
                }
                is NetworkResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoadingCategories = false,
                        categoriesError = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    _state.value = _state.value.copy(isLoadingCategories = true)
                }
            }
        }
    }

    private fun sortProducts(sortBy: String, order: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                sortBy = sortBy,
                sortOrder = order,
                isLoading = true
            )

            val result = getProductsUseCase(pageSize, 0, sortBy, order)

            when (result) {
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(
                        products = result.data.products,
                        isLoading = false,
                        currentPage = 1,
                        hasNextPage = result.data.hasNextPage,
                        totalProducts = result.data.total
                    )
                }
                is NetworkResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun navigateToProductDetails(productId: Int) {
        viewModelScope.launch {
            _effects.emit(ProductsEffect.NavigateToProductDetails(productId))
        }
    }

    private fun retryLastAction() {
        val currentState = _state.value
        when {
            currentState.searchQuery.isNotEmpty() -> searchProducts(currentState.searchQuery)
            currentState.selectedCategory != null -> filterByCategory(currentState.selectedCategory)
            else -> loadProducts()
        }
    }
}