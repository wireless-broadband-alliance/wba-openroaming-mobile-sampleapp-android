plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.galactikperspective.mimwifi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.galactikperspective.mimwifi"

        minSdk = 30
        targetSdk = 34

        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {

        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":or-sdk"))

    implementation("com.google.android.material:material:1.12.0")
    implementation("com.google.android.play:core-ktx:1.8.1")

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.airbnb.android:lottie:6.5.0")
}