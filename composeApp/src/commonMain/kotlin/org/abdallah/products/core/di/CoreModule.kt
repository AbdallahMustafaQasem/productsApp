package org.abdallah.products.core.di

import io.ktor.client.*
import org.abdallah.products.core.network.HttpClientFactory
import org.koin.dsl.module

val coreModule = module {
    
    // HTTP Client
    single<HttpClient> { 
        HttpClientFactory.create() 
    }
    
}

