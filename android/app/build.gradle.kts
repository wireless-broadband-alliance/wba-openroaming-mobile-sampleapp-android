import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tetrapi.or"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tetrapi.or"

        minSdk = 30
        targetSdk = 35

        versionCode = 5
        versionName = "1.1.0"
    }

    buildTypes {

        release {
            isMinifyEnabled = false
            isShrinkResources = false

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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }
}

dependencies {
    implementation(platform("io.grpc:grpc-bom:1.76.0"))
    implementation("com.google.protobuf:protobuf-java:4.33.0")
    
    val debug = gradle.startParameter.taskNames.any { it.contains("Debug") }
    if (debug) implementation(project(":or-sdk")) else implementation("com.tetrapi.sdk:or:1.1.0")

    implementation("com.tetrapi.sdk:or:1.1.0")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.airbnb.android:lottie:6.7.0")
}