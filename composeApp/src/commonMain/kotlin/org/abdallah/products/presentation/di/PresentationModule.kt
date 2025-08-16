package org.abdallah.products.presentation.di

import org.abdallah.products.presentation.screens.details.ProductDetailsViewModel
import org.abdallah.products.presentation.screens.products.ProductsViewModel
import org.koin.dsl.module

val presentationModule = module {
    
    // ViewModels
    factory { 
        ProductsViewModel(
            getProductsUseCase = get(),
            searchProductsUseCase = get(),
            getCategoriesUseCase = get(),
            getProductsByCategoryUseCase = get()
        ) 
    }
    
    factory { 
        ProductDetailsViewModel(
            getProductDetailsUseCase = get()
        ) 
    }
}
