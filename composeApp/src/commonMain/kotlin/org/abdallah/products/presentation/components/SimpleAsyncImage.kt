package org.abdallah.products.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

/**
 * Simple AsyncImage replacement that shows a placeholder
 * In a real app, you would implement actual image loading here
 */
@Composable
fun SimpleAsyncImage(
    model: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    placeholder: @Composable (() -> Unit)? = null
) {
    // Clean placeholder without debug info
    Box(
        modifier = modifier.background(
            MaterialTheme.colorScheme.surfaceVariant,
            RoundedCornerShape(8.dp)
        ),
        contentAlignment = Alignment.Center
    ) {
        placeholder?.invoke() ?: Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = contentDescription,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}

// Alternative: You can implement actual image loading using platform-specific code
// or a simpler image loading library that doesn't have dependency conflicts
