package org.abdallah.products.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.abdallah.products.core.network.NetworkResult
import org.abdallah.products.domain.usecase.GetProductDetailsUseCase

class ProductDetailsViewModel(
    private val getProductDetailsUseCase: GetProductDetailsUseCase
) : ViewModel() {

    // Private mutable state
    private val _state = MutableStateFlow(ProductDetailsState())
    val state: StateFlow<ProductDetailsState> = _state.asStateFlow()

    // Effects channel
    private val _effects = MutableSharedFlow<ProductDetailsEffect>()
    val effects: SharedFlow<ProductDetailsEffect> = _effects.asSharedFlow()

    fun handleIntent(intent: ProductDetailsIntent) {
        when (intent) {
            is ProductDetailsIntent.LoadProductDetails -> loadProductDetails(intent.productId)
            is ProductDetailsIntent.RefreshProductDetails -> refreshProductDetails()
            is ProductDetailsIntent.NavigateBack -> navigateBack()
            is ProductDetailsIntent.ShareProduct -> shareProduct(intent.product)
            is ProductDetailsIntent.RetryLoading -> retryLoading()
        }
    }

    private fun loadProductDetails(productId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                productId = productId,
                isLoading = true,
                error = null
            )

            when (val result = getProductDetailsUseCase(productId)) {
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(
                        productDetails = result.data,
                        isLoading = false
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

    private fun refreshProductDetails() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true, error = null)

            when (val result = getProductDetailsUseCase(_state.value.productId)) {
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(
                        productDetails = result.data,
                        isRefreshing = false
                    )
                }
                is NetworkResult.Error -> {
                    _state.value = _state.value.copy(
                        isRefreshing = false,
                        error = result.message
                    )
                    _effects.emit(ProductDetailsEffect.ShowSnackbar("Failed to refresh product details"))
                }
                is NetworkResult.Loading -> {
                    _state.value = _state.value.copy(isRefreshing = true)
                }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _effects.emit(ProductDetailsEffect.NavigateBack)
        }
    }

    private fun shareProduct(product: org.abdallah.products.domain.entities.ProductDetails) {
        viewModelScope.launch {
            val shareText = buildString {
                append("Check out this product: ${product.title}\n")
                append("Price: $${product.price}\n")
                if (product.brand != null) {
                    append("Brand: ${product.brand}\n")
                }
                append("Rating: ${product.rating}/5\n")
                append("\n${product.description}")
            }
            _effects.emit(ProductDetailsEffect.ShareProduct(shareText))
        }
    }

    private fun retryLoading() {
        loadProductDetails(_state.value.productId)
    }
}

