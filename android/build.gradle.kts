import org.cyclonedx.Version
import org.cyclonedx.gradle.CyclonedxDirectTask

plugins {
    id("com.android.application") version "9.4.0" apply false
    id("com.android.library") version "9.4.0" apply false

    id("com.google.gms.google-services") version "4.5.0" apply false
    id("com.google.firebase.crashlytics") version "3.0.8" apply false

    id("org.cyclonedx.bom") version "3.4.1"
}

tasks.withType<CyclonedxDirectTask>().configureEach {
    includeConfigs.set(listOf("releaseRuntimeClasspath"))
    schemaVersion.set(Version.VERSION_15)
}

tasks.cyclonedxBom {
    schemaVersion = Version.VERSION_17
    jsonOutput = file("build/reports/gl-sbom-android.json")
    xmlOutput.unsetConvention()
}

subprojects {

    configurations.all {

        exclude(group = "javax.annotation", module = "javax.annotation-api")
        exclude(group = "net.java.dev.jna")
        exclude(group = "xpp3", module = "xpp3")
        exclude(group = "net.sf.kxml", module = "kxml2")
        exclude(group = "org.junit.platform")
        exclude(group = "org.junit.jupiter")
        exclude(group = "junit", module = "junit")

        resolutionStrategy {

            force(
                "com.google.guava:guava:33.6.0-android",
                "org.apache.httpcomponents:httpclient:4.5.13",
                "org.apache.commons:commons-lang3:3.18.0"
            )

            eachDependency {

                when (requested.group) {
                    "io.netty" -> useVersion("4.2.17.Final")
                    "com.google.protobuf" -> useVersion("4.35.0")
                    "org.bouncycastle" -> useVersion("1.86")
                }
            }
        }
    }
}