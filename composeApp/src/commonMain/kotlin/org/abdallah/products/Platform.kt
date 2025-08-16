package org.abdallah.products

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform