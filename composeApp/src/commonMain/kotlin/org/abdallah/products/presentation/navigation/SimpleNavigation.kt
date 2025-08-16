package org.abdallah.products.presentation.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.abdallah.products.presentation.screens.details.ProductDetailsScreen
import org.abdallah.products.presentation.screens.products.ProductsScreen

// Simple navigation without external dependencies
sealed class Screen {
    object Products : Screen()
    data class ProductDetails(val productId: Int) : Screen()
}

@Composable
fun SimpleProductNavigation(
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Products) }
    
    when (val screen = currentScreen) {
        is Screen.Products -> {
            ProductsScreen(
                onNavigateToDetails = { productId ->
                    currentScreen = Screen.ProductDetails(productId)
                },
                modifier = modifier
            )
        }
        
        is Screen.ProductDetails -> {
            ProductDetailsScreen(
                productId = screen.productId,
                onNavigateBack = {
                    currentScreen = Screen.Products
                },
                modifier = modifier
            )
        }
    }
}

