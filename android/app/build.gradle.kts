import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")

    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tetrapi.or"

    compileSdk {

        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.tetrapi.or"

        minSdk = 30
        targetSdk = 36

        versionCode = 16
        versionName = "1.5.0"
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
    implementation(platform("io.grpc:grpc-bom:1.80.0"))
    implementation("com.google.protobuf:protobuf-java:4.34.1")

    implementation(platform("com.google.firebase:firebase-bom:34.12.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")

    implementation(project(":or-sdk"))

    /*
    val debug = gradle.startParameter.taskNames.any { it.contains("Debug") }
    if (debug) implementation(project(":or-sdk")) else implementation("com.tetrapi.sdk:or:1.1.0")
    */

    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
}