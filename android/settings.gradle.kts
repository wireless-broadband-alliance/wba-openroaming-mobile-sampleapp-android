import org.gradle.authentication.http.HttpHeaderAuthentication
import org.gradle.api.credentials.HttpHeaderCredentials
import java.util.Properties

pluginManagement {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    val env = Properties().apply {
        val propsFile = File(rootDir, "local.properties")
        if (propsFile.exists()) {
            load(propsFile.inputStream())
        }
    }

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Load token from local.properties if available
        val propsFile = File(rootDir, "local.properties")
        val localProps = Properties()
        if (propsFile.exists()) {
            localProps.load(propsFile.inputStream())
        }

        val localToken = localProps.getProperty("GITLAB_TOKEN")
        val ciToken = System.getenv("CI_JOB_TOKEN")

        // Use project Maven repository (private)
        maven {
            url = uri("https://git.tetrapi.pt/api/v4/projects/208/packages/maven")

            when {
                // ✅ Case 1: Running inside GitLab CI
                !ciToken.isNullOrEmpty() -> {
                    credentials(HttpHeaderCredentials::class) {
                        name = "Job-Token"
                        value = ciToken
                    }
                    authentication {
                        create<HttpHeaderAuthentication>("header")
                    }
                    println("🔐 Using CI_JOB_TOKEN for GitLab Maven access")
                }

                // ✅ Case 2: Running locally (manual publish / build)
                !localToken.isNullOrEmpty() -> {
                    credentials(HttpHeaderCredentials::class) {
                        name = "Private-Token"
                        value = localToken
                    }
                    authentication {
                        create<HttpHeaderAuthentication>("header")
                    }
                    println("🔐 Using local GITLAB_TOKEN from local.properties")
                }

                // ⚠️ No token — warn user
                else -> {
                    println("⚠️ Warning: No GitLab token found. Private Maven packages may fail to resolve.")
                }
            }
        }

        /*
        maven {
            url = uri("https://git.tetrapi.pt/api/v4/projects/208/packages/maven")

            credentials(HttpHeaderCredentials::class) {
                name = "Private-Token"
                value = env.getProperty("GITLAB_TOKEN")
            }

            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }

         */
    }
}

rootProject.name = "GRA ID SDK"
include(":app")