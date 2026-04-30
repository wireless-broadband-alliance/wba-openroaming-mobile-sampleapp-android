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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        /*
        maven {
            url = uri("https://git.tetrapi.pt/api/v4/projects/208/packages/maven")

            credentials(HttpHeaderCredentials::class) {
                if (System.getenv("CI_JOB_TOKEN") != null) {
                    name = "Job-Token"
                    value = System.getenv("CI_JOB_TOKEN")
                } else {
                    name = "Private-Token"
                    value = runCatching {
                        val properties = Properties().apply {
                            load(rootDir.resolve("local.properties").inputStream())
                        }

                        properties.getProperty("GITLAB_TOKEN")
                    }.getOrNull()
                }
            }

            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
        */
    }
}

rootProject.name = "OpenRoaming Mobile"
include(":app", ":or-sdk")