# Product Browser App

A cross-platform mobile application built with Kotlin Multiplatform that allows users to browse and search products from the DummyJSON API.

## Business Requirements

This application addresses the following key requirements:

### Core Features
- **Product Listing**: Display a comprehensive list of products with pagination support
- **Product Search**: Real-time search functionality with query validation
- **Product Details**: Detailed view of individual products including images, pricing, and specifications
- **Category Filtering**: Browse products by categories for better organization
- **Offline Support**: Basic caching mechanism for improved user experience

### User Experience
- **Pull-to-Refresh**: Manual data refresh capability
- **Loading States**: Clear visual feedback during network operations
- **Error Handling**: Graceful error management with user-friendly messages
- **Responsive Design**: Optimized for various screen sizes and orientations

## Project Architecture

### Architecture Pattern
The application follows **Clean Architecture** principles with **MVI (Model-View-Intent)** pattern for the presentation layer.

```
├── presentation/        # UI Layer (Compose UI, ViewModels, States)
├── domain/             # Business Logic (Use Cases, Entities, Repository Interfaces)
├── data/              # Data Layer (Repository Implementation, Network, Local Storage)
└── core/              # Shared Utilities (Network, DI, Extensions)
```

### Key Components

#### Presentation Layer
- **Compose Multiplatform** for cross-platform UI
- **StateFlow** for reactive state management
- **Custom Navigation** implementation (avoiding third-party dependencies)
- **Custom AsyncImage** component for image loading

#### Domain Layer
- **Use Cases** encapsulating business logic
- **Repository Pattern** for data abstraction
- **Entity Models** representing business objects

#### Data Layer
- **Ktor Client** for network operations
- **Kotlinx Serialization** for JSON parsing
- **In-memory caching** for improved performance
- **Repository Implementation** with error handling

#### Dependency Injection
- **Koin** for lightweight dependency injection across platforms

### Technology Stack
- **Kotlin Multiplatform** - Cross-platform development
- **Compose Multiplatform** - Declarative UI framework
- **Ktor** - HTTP client for API communication
- **Kotlinx Coroutines** - Asynchronous programming
- **Kotlinx Serialization** - JSON serialization
- **Koin** - Dependency injection
- **Material Design 3** - Modern UI components

## Build and Run Instructions

### Prerequisites
- **Android Studio** (latest stable version)
- **JDK 11** or higher
- **Xcode 14+** (for iOS development, macOS only)
- **iOS Simulator** or physical iOS device (for iOS testing)

### Android Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ProductBrowserApp
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory

3. **Sync dependencies**
   ```bash
   ./gradlew build
   ```

4. **Run on Android**
   - Connect an Android device or start an emulator
   - Select the `composeApp` configuration
   - Click "Run" or use `Shift + F10`

   **Command line alternative:**
   ```bash
   ./gradlew :composeApp:installDebug
   ```

### iOS Setup (macOS only)

1. **Install Xcode**
   - Download from Mac App Store
   - Install iOS simulators

2. **Generate iOS project**
   ```bash
   ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
   ```

3. **Open iOS project**
   ```bash
   open iosApp/iosApp.xcodeproj
   ```

4. **Run on iOS**
   - Select target device/simulator in Xcode
   - Press `Cmd + R` to build and run

### Testing

Run unit tests:
```bash
./gradlew test
```

Run Android-specific tests:
```bash
./gradlew :composeApp:testDebugUnitTest
```

## Development Trade-offs and Assumptions

### Technical Decisions

#### 1. Custom Navigation vs. Third-party Libraries
**Decision**: Implemented custom navigation system
**Reason**: Avoided dependency conflicts with Compose Multiplatform versions
**Trade-off**: More development time but better version control and customization

#### 2. Custom Image Loading vs. External Libraries
**Decision**: Used Compose's built-in AsyncImage with custom wrapper
**Reason**: Reduced dependencies and potential compatibility issues
**Trade-off**: Less advanced features but better stability across platforms

#### 3. In-memory Caching vs. Persistent Storage
**Decision**: Implemented simple in-memory caching
**Reason**: Faster implementation and sufficient for current requirements
**Trade-off**: Data lost on app restart but better performance

#### 4. Manual State Management vs. State Management Libraries
**Decision**: Used StateFlow with custom state management
**Reason**: Leverages Kotlin Coroutines' native capabilities
**Trade-off**: More boilerplate but predictable behavior

### API Assumptions

- **DummyJSON API** is stable and available
- **Product data structure** remains consistent
- **Network connectivity** is handled gracefully with appropriate fallbacks
- **Image URLs** from API are valid and accessible

### Platform Considerations

#### Android
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 35 (Android 15)
- **Permissions**: Internet access for API calls

#### iOS
- **Minimum version**: iOS 14.0
- **Architecture support**: arm64, x86_64 (simulator)
- **Network security**: Allows HTTP traffic for development

### Performance Considerations

- **Lazy loading** implemented for product lists
- **Image caching** handled by platform-specific implementations
- **Memory management** optimized for mobile constraints
- **Network request batching** for efficient API usage

### Known Limitations

1. **Offline functionality** is limited to cached data
2. **Search suggestions** not implemented
3. **Advanced filtering** (price range, ratings) not available
4. **User preferences** not persisted between sessions
5. **Analytics** and crash reporting not integrated

## Future Enhancements

- Persistent local database (Room/SQLite)
- Advanced search with filters and sorting
- User favorites and wishlist functionality
- Push notifications for new products
- Social sharing capabilities
- Dark mode theme implementation

## API Reference

This app uses the [DummyJSON API](https://dummyjson.com/) for product data:

- **Products**: `https://dummyjson.com/products`
- **Search**: `https://dummyjson.com/products/search`
- **Categories**: `https://dummyjson.com/products/categories`
- **Product Details**: `https://dummyjson.com/products/{id}`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is part of a technical assessment and is for educational purposes.
