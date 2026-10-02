# OpenRoaming Android Sample App

A reference Android application demonstrating how to integrate and use the **OpenRoaming Android SDK** for Wi-Fi Passpoint (Hotspot 2.0) provisioning, user authentication, Cloudflare Turnstile protection, and remote configuration management.

---

## 📱 Overview

This sample app serves as an end-to-end implementation guide for developers looking to integrate OpenRoaming Wi-Fi connectivity into their Android applications. It demonstrates:

- **SDK Initialization:** Automatic bootstrapping via AndroidX App Startup (`androidx.startup`).
- **Remote Configuration:** Fetching dynamic platform and SDK settings.
- **User Authentication:** 
  - Local login with username/password.
  - Two-Factor Authentication (2FA / TOTP) handling.
  - SAML Single Sign-On (SSO) flow integration.
- **Cloudflare Turnstile:** Capturing and passing anti-bot verification tokens via `WebView`.
- **Passpoint Connection:** One-tap Wi-Fi profile generation, encrypted RADIUS credential exchange, and network installation (`WifiNetworkSuggestion`).
- **Network Management:** Viewing and removing active Passpoint configurations.

---

## 📋 Requirements

- **Android Studio:** Ladybug / Jellyfish or newer.
- **Android SDK:** API Level 26 (Android 8.0) or higher.
- **Physical Device:** Passpoint (Hotspot 2.0) features require a physical Android device with Wi-Fi hardware enabled (Android Emulators do not support Wi-Fi Passpoint hardware features).
- **Kotlin:** 1.9+

---

## 🚀 Getting Started

### 1. Clone the Repository
Clone the repository along with the SDK module/submodule:

```bash
git clone https://github.com/wireless-broadband-alliance/wba-openroaming-mobile-sampleapp-android
cd wba-openroaming-mobile-sampleapp-android
```

### 2. Configure API Endpoints
Open `or-sdk/src/main/res/values/strings.xml` and update the `open_roaming_api` string with your backend environment base URL:

```xml
<resources>
    <string name="open_roaming_api">https://your-api-domain.com/v1/%1$s</string>
    <string name="open_roaming_not_supported">Your device does not support Wi-Fi Passpoint (OpenRoaming).</string>
</resources>
```

### 3. Build & Run
1. Connect a physical Android device with Wi-Fi enabled.
2. Open the project in Android Studio.
3. Sync Gradle and press **Run** (`Shift + F10`).

---

## 📂 Project Structure

```text
app/
 ├── src/main/
 │    ├── java/ (or kotlin/) com/wba/or/
 │    │    ├── activities/                   # Application activities and screens
 │    │    │    ├── AboutActivity.kt         # Information & About screen
 │    │    │    ├── LoginActivity.kt         # User authentication screen
 │    │    │    ├── MainActivity.kt          # Main dashboard activity
 │    │    │    ├── ORActivity.kt            # OpenRoaming network provisioning & management
 │    │    │    ├── RegisterActivity.kt      # New user account registration
 │    │    │    ├── ResetActivity.kt         # Password reset screen
 │    │    │    ├── TwoFAActivity.kt         # 2FA code verification screen
 │    │    │    ├── TwoFAConfigurationActivity.kt # 2FA / TOTP setup activity
 │    │    │    └── UserActivity.kt          # User profile activity
 │    │    ├── adapters/                     # RecyclerView adapters
 │    │    │    └── NetworksAdapter.kt       # Adapter for displaying network items
 │    │    ├── dialogs/                      # Modal dialogs (loading & confirmations)
 │    │    │    ├── DeleteDialog.kt
 │    │    │    └── LoadingDialog.kt
 │    │    └── views/                        # Custom UI components
 │    │         └── StateMaterialButton.kt
 │    └── res/                               # XML layouts, drawables, and strings
 │  
 └── build.gradle.kts
or-sdk/                                      # OpenRoaming SDK local module
```

---

## 💡 How SDK Features Are Implemented

### 1. Fetching SDK Configuration
```kotlin
lifecycleScope.launch {
    SDK.getConfig().fetch()
        .onSuccess { config ->
            // Update UI with platform settings
        }
        .onFailure { error ->
            // Handle error
        }
}
```

### 2. Turnstile & Authentication Flow
```kotlin
// Render Cloudflare Turnstile challenge in WebView
SDK.getTurnstile().webView(
    webView = binding.turnstileWebView,
    onSuccess = { turnstileToken ->
        // Perform login with the obtained Turnstile token
        performLogin(turnstileToken)
    },
    onError = { error ->
        // Handle Turnstile error
    }
)
```

### 3. Connecting to OpenRoaming
```kotlin
val openRoaming = OpenRoaming()

openRoaming.onConnectionSuccess = {
    Toast.makeText(this, "Successfully connected to OpenRoaming!", Toast.LENGTH_SHORT).show()
}

openRoaming.onConnectionError = { errorMessage ->
    Toast.makeText(this, "Connection failed: $errorMessage", Toast.LENGTH_LONG).show()
}

// Triggers key generation and profile installation
openRoaming.connect(this)
```

---

## 📄 License

Copyright © 2026 **WBA (Wireless Broadband Alliance)**. All rights reserved.
