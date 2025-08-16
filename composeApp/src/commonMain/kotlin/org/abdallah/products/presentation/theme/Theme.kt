package org.abdallah.products.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Blue600,
    onPrimary = White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue800,
    
    secondary = Orange600,
    onSecondary = White,
    secondaryContainer = Orange50,
    onSecondaryContainer = Orange700,
    
    tertiary = Gray600,
    onTertiary = White,
    tertiaryContainer = Gray100,
    onTertiaryContainer = Gray800,
    
    error = Error,
    onError = White,
    errorContainer = ErrorLight,
    onErrorContainer = Error,
    
    background = White,
    onBackground = Gray900,
    surface = White,
    onSurface = Gray900,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = Gray700,
    
    outline = Gray400,
    outlineVariant = Gray300,
    scrim = Black.copy(alpha = 0.32f),
    
    inverseSurface = Gray800,
    inverseOnSurface = Gray50,
    inversePrimary = Blue100,
    
    surfaceTint = Blue600
)



@Composable
fun ProductBrowserTheme(
    content: @Composable () -> Unit
) {
    // Always use light color scheme, ignore system theme
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

// Custom color extensions
val ColorScheme.priceColor: androidx.compose.ui.graphics.Color
    get() = PriceColor

val ColorScheme.discountColor: androidx.compose.ui.graphics.Color
    get() = DiscountColor

val ColorScheme.ratingColor: androidx.compose.ui.graphics.Color
    get() = RatingColor

val ColorScheme.stockLowColor: androidx.compose.ui.graphics.Color
    get() = StockLowColor

val ColorScheme.stockHighColor: androidx.compose.ui.graphics.Color
    get() = StockHighColor
