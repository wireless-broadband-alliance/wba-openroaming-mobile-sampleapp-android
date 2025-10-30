import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.authentication.http.HttpHeaderAuthentication
import org.gradle.api.credentials.HttpHeaderCredentials
import java.util.Properties

private val env = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.tetrapi.sdk"
    compileSdk = 36

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

    kotlin {

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }
}

dependencies {
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
    implementation("io.ktor:ktor-client-core:3.3.1")
    implementation("io.ktor:ktor-client-cio:3.3.1")
    implementation("com.squareup.okhttp3:okhttp:5.2.1")
}

afterEvaluate {

    publishing {

        publications {

            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.tetrapi.sdk"
                artifactId = "or"
                version = "1.1.0"

                pom {
                    name.set("or")
                    description.set("Internal SDK library for OpenRoaming Android app")
                    url.set("https://git.tetrapi.pt/tcs/wba/wba-openroaming-android")
                }
            }
        }

        repositories {

            maven {
                name = "GitLab"
                url = uri("https://git.tetrapi.pt/api/v4/projects/208/packages/maven")

                credentials(HttpHeaderCredentials::class) {
                    name = "Private-Token"
                    value = env.getProperty("GITLAB_TOKEN")
                }

                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
        }
    }
}