package org.abdallah.products.domain.di

import org.abdallah.products.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    
    // Use Cases
    factory { GetProductsUseCase(repository = get()) }
    factory { SearchProductsUseCase(repository = get()) }
    factory { GetProductDetailsUseCase(repository = get()) }
    factory { GetCategoriesUseCase(repository = get()) }
    factory { GetProductsByCategoryUseCase(repository = get()) }
}

