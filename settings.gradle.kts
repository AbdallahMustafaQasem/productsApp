rootProject.name = "ProductBrowserApp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        
        // JetBrains Compose Multiplatform repository
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        
        // Additional repositories for Compose dependencies
        maven("https://androidx.dev/storage/compose-compiler/repository/")
    }
}

include(":composeApp")