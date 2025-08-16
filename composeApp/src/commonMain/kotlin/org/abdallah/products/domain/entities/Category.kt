package org.abdallah.products.domain.entities

/**
 * Category entity
 */
data class Category(
    val name: String,
    val slug: String
) {
    val displayName: String get() = name.replaceFirstChar { 
        if (it.isLowerCase()) it.titlecase() else it.toString() 
    }
}

