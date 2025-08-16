package org.abdallah.products.presentation.screens.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.abdallah.products.presentation.components.*
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    onNavigateToDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductsViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    val gridState = rememberLazyGridState()
    
    // Handle effects
    LaunchedEffect(viewModel.effects) {
        viewModel.effects
            .onEach { effect ->
                when (effect) {
                    is ProductsEffect.NavigateToProductDetails -> {
                        onNavigateToDetails(effect.productId)
                    }
                    is ProductsEffect.ScrollToTop -> {
                        gridState.animateScrollToItem(0)
                    }
                    is ProductsEffect.ShowSnackbar -> {
                        // Handle snackbar - you can implement this with a SnackbarHost
                    }
                }
            }
            .collect()
    }
    
    // Handle load more when reaching the end
    LaunchedEffect(gridState.canScrollForward) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && 
                    lastVisibleIndex >= state.products.size - 5 && 
                    state.canLoadMore) {
                    viewModel.handleIntent(ProductsIntent.LoadNextPage)
                }
            }
    }
    
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top App Bar
            TopAppBar(
                title = { 
                    Text(
                        text = "Products",
                        style = MaterialTheme.typography.headlineMedium
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
            // Search Bar
            SearchBar(
                searchQuery = state.searchQuery,
                onSearchQueryChanged = { query ->
                    if (query != state.searchQuery) {
                        viewModel.handleIntent(ProductsIntent.SearchProducts(query))
                    }
                },
                onSearchSubmit = { query ->
                    viewModel.handleIntent(ProductsIntent.SearchProducts(query))
                },
                onClearSearch = {
                    viewModel.handleIntent(ProductsIntent.ClearSearch)
                },
                modifier = Modifier.padding(16.dp)
            )
            
            // Category Filter
            if (state.isLoadingCategories) {
                CategoryFilterShimmer(
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            } else {
                CategoryFilter(
                    categories = state.categories,
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = { category ->
                        viewModel.handleIntent(ProductsIntent.FilterByCategory(category))
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            // Content
            when {
                state.isInitialLoading -> {
                    LoadingIndicator(
                        modifier = Modifier.fillMaxSize(),
                        message = "Loading products..."
                    )
                }
                
                state.showEmptyState -> {
                    EmptyState(
                        message = when {
                            state.searchQuery.isNotEmpty() -> 
                                "No products found for \"${state.searchQuery}\""
                            state.selectedCategory != null -> 
                                "No products found in ${state.selectedCategory?.displayName ?: "category"}"
                            else -> "No products available"
                        },
                        actionText = if (state.error != null) "Retry" else null,
                        onAction = if (state.error != null) {
                            { viewModel.handleIntent(ProductsIntent.RetryLastAction) }
                        } else null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                state.error != null && state.products.isEmpty() -> {
                    ErrorMessage(
                        message = state.error ?: "Unknown error",
                        onRetry = { viewModel.handleIntent(ProductsIntent.RetryLastAction) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        state = gridState,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = state.products,
                            key = { product -> "product_${product.id}" }
                        ) { product ->
                            ProductCard(
                                product = product,
                                onClick = { productId ->
                                    viewModel.handleIntent(
                                        ProductsIntent.NavigateToProductDetails(productId)
                                    )
                                }
                            )
                        }
                        
                        // Loading more indicator
                        if (state.isLoadingMore) {
                            item(
                                key = "loading_more",
                                span = { GridItemSpan(maxLineSpan) }
                            ) {
                                LoadMoreIndicator()
                            }
                        }
                        
                        // Error for load more
                        if (state.error != null && state.products.isNotEmpty()) {
                            item(
                                key = "load_more_error",
                                span = { GridItemSpan(maxLineSpan) }
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Failed to load more",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                        TextButton(
                                            onClick = { 
                                                viewModel.handleIntent(ProductsIntent.LoadNextPage) 
                                            }
                                        ) {
                                            Text("Retry")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
