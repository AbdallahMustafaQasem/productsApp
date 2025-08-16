package org.abdallah.products.core.constants

object ApiConstants {
    
    // Base URL
    const val BASE_URL = "https://dummyjson.com/"
    
    // Endpoints
    const val PRODUCTS = "products"
    const val PRODUCTS_SEARCH = "products/search"
    const val PRODUCT_DETAIL = "products/{id}"
    const val CATEGORIES = "products/category-list"
    const val PRODUCTS_BY_CATEGORY = "products/category/{category}"
    
    // Query Parameters
    const val LIMIT = "limit"
    const val SKIP = "skip"
    const val QUERY = "q"
    const val SORT_BY = "sortBy"
    const val ORDER = "order"
    const val SELECT = "select"
    
    // Default Values
    const val DEFAULT_LIMIT = 30
    const val DEFAULT_PAGE_SIZE = 20
    const val MAX_RETRY_COUNT = 3
    

    
}
