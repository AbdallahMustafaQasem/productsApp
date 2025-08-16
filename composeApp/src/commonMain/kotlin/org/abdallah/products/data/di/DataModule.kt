package org.abdallah.products.data.di

import org.abdallah.products.data.remote.api.ProductsApi
import org.abdallah.products.data.remote.api.ProductsApiImpl
import org.abdallah.products.data.repository.ProductRepositoryImpl
import org.abdallah.products.domain.repository.ProductRepository
import org.koin.dsl.module

val dataModule = module {
    
    // API
    single<ProductsApi> { 
        ProductsApiImpl(httpClient = get()) 
    }
    
    // Repository
    single<ProductRepository> { 
        ProductRepositoryImpl(api = get()) 
    }
}

