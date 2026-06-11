plugins {
    id("com.android.library")
    id("maven-publish")
}

android {
    namespace = "com.tetrapi.sdk"

    compileSdk {

        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 30
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {

        release {
            isMinifyEnabled = false
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

    publishing {

        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation("com.google.android.material:material:1.13.0")
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")

    implementation("io.ktor:ktor-client-core:3.4.3")
    implementation("io.ktor:ktor-client-cio:3.4.3")
}