package org.abdallah.products

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.abdallah.products.di.appModules
import org.abdallah.products.presentation.navigation.SimpleProductNavigation
import org.abdallah.products.presentation.theme.ProductBrowserTheme
import org.koin.compose.KoinApplication
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(appModules)
    }) {
        ProductBrowserTheme {
            SimpleProductNavigation(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}