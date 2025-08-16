package org.abdallah.products.presentation.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
// import coil3.compose.AsyncImage
import org.abdallah.products.presentation.components.SimpleAsyncImage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.abdallah.products.core.utils.formatPrice
import org.abdallah.products.core.utils.formatRating
import org.abdallah.products.domain.entities.ProductDetails
import org.abdallah.products.domain.entities.Review
import org.abdallah.products.presentation.components.ErrorMessage
import org.abdallah.products.presentation.components.LoadingIndicator

import org.abdallah.products.presentation.theme.*
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailsViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    
    // Initialize the screen
    LaunchedEffect(productId) {
        viewModel.handleIntent(ProductDetailsIntent.LoadProductDetails(productId))
    }
    
    // Handle effects
    LaunchedEffect(viewModel.effects) {
        viewModel.effects
            .onEach { effect ->
                when (effect) {
                    is ProductDetailsEffect.NavigateBack -> {
                        onNavigateBack()
                    }
                    is ProductDetailsEffect.ShareProduct -> {
                        // Handle sharing - you can implement platform-specific sharing
                    }
                    is ProductDetailsEffect.ShowSnackbar -> {
                        // Handle snackbar
                    }
                }
            }
            .collect()
    }
    
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            state.isLoading && state.productDetails == null -> {
                LoadingIndicator(
                    modifier = Modifier.fillMaxSize(),
                    message = "Loading product details..."
                )
            }
            
            state.showError -> {
                ErrorMessage(
                    message = state.error ?: "Failed to load product details",
                    onRetry = { viewModel.handleIntent(ProductDetailsIntent.RetryLoading) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            state.hasProduct -> {
                ProductDetailsContent(
                    product = state.productDetails!!,
                    onNavigateBack = { viewModel.handleIntent(ProductDetailsIntent.NavigateBack) },
                    onShare = { viewModel.handleIntent(ProductDetailsIntent.ShareProduct(it)) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailsContent(
    product: ProductDetails,
    onNavigateBack: () -> Unit,
    onShare: (ProductDetails) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Product Details") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = { onShare(product) }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share"
                    )
                }
            }
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProductImageGallery(images = product.images)
            }
            
            item {
                ProductInfoSection(product = product)
            }
            
            item {
                ProductDescriptionSection(description = product.description)
            }
            
            if (product.reviews.isNotEmpty()) {
                item {
                    ProductReviewsSection(reviews = product.reviews)
                }
            }
            
            item {
                ProductSpecsSection(product = product)
            }
        }
    }
}

@Composable
private fun ProductImageGallery(
    images: List<String>
) {
    if (images.isNotEmpty()) {
        val pagerState = rememberPagerState(pageCount = { images.size })
        
        Column {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) { page ->
                SimpleAsyncImage(
                    model = images[page],
                    contentDescription = "Product image ${page + 1}",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )
            }
            
            if (images.size > 1) {
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(images.size) { index ->
                        SimpleAsyncImage(
                            model = images[index],
                            contentDescription = "Thumbnail ${index + 1}",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductInfoSection(product: ProductDetails) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Category
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = product.category.uppercase(),
                    style = ProductTypography.ProductCategory,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Title
            Text(
                text = product.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            
            // Brand
            product.brand?.let { brand ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "by $brand",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Rating and Stock
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.rating.formatRating(),
                    style = ProductTypography.ProductRating,
                    color = MaterialTheme.colorScheme.ratingColor
                )
                
                Text(
                    text = if (product.stock > 0) "${product.stock} in stock" else "Out of stock",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (product.stock > 10) {
                        MaterialTheme.colorScheme.stockHighColor
                    } else if (product.stock > 0) {
                        MaterialTheme.colorScheme.stockLowColor
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = product.price.formatPrice(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.priceColor,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (product.discountPercentage > 0) {
                        val originalPrice = product.price / (1 - product.discountPercentage / 100)
                        Text(
                            text = originalPrice.formatPrice(),
                            style = ProductTypography.ProductOriginalPrice,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
                
                if (product.discountPercentage > 0) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.discountColor
                    ) {
                        Text(
                            text = "-${product.discountPercentage.toInt()}%",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onError,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductDescriptionSection(description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = description,
                style = ProductTypography.ProductDescription,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProductReviewsSection(reviews: List<Review>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Reviews (${reviews.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            reviews.take(3).forEach { review ->
                ReviewItem(review = review)
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            if (reviews.size > 3) {
                Text(
                    text = "and ${reviews.size - 3} more reviews...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ReviewItem(review: Review) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = review.reviewerName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = "★".repeat(review.rating),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.ratingColor
            )
        }
        
        Text(
            text = review.comment,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProductSpecsSection(product: ProductDetails) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Specifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            val specs = buildList {
                product.sku?.let { add("SKU" to it) }
                product.weight?.let { add("Weight" to "${it} kg") }
                product.dimensions?.let { dims ->
                    add("Dimensions" to "${dims.width} × ${dims.height} × ${dims.depth} cm")
                }
                product.warrantyInformation?.let { add("Warranty" to it) }
                product.shippingInformation?.let { add("Shipping" to it) }
                product.returnPolicy?.let { add("Return Policy" to it) }
                product.availabilityStatus?.let { add("Availability" to it) }
                product.minimumOrderQuantity?.let { add("Min. Order" to "$it units") }
            }
            
            specs.forEach { (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
