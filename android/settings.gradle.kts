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
    }
}

rootProject.name = "GRA ID SDK"
include(":app")