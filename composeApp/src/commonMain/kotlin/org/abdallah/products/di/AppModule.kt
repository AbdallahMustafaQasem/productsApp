package org.abdallah.products.di

import org.abdallah.products.core.di.coreModule
import org.abdallah.products.data.di.dataModule
import org.abdallah.products.domain.di.domainModule
import org.abdallah.products.presentation.di.presentationModule

val appModules = listOf(
    coreModule,
    dataModule,
    domainModule,
    presentationModule
)
