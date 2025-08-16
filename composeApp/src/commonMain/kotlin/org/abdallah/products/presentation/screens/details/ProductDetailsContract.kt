package org.abdallah.products.presentation.screens.details

import org.abdallah.products.domain.entities.ProductDetails

/**
 * MVI Contract for Product Details Screen
 */

// User Intents
sealed class ProductDetailsIntent {
    data class LoadProductDetails(val productId: Int) : ProductDetailsIntent()
    data object RefreshProductDetails : ProductDetailsIntent()
    data object NavigateBack : ProductDetailsIntent()
    data class ShareProduct(val product: ProductDetails) : ProductDetailsIntent()
    data object RetryLoading : ProductDetailsIntent()
}

// UI State
data class ProductDetailsState(
    val productDetails: ProductDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val productId: Int = -1
) {
    val hasProduct: Boolean = productDetails != null
    val showError: Boolean = error != null && productDetails == null
}

// Side Effects
sealed class ProductDetailsEffect {
    data object NavigateBack : ProductDetailsEffect()
    data class ShareProduct(val shareText: String) : ProductDetailsEffect()
    data class ShowSnackbar(val message: String) : ProductDetailsEffect()
}

