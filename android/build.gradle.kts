plugins {
    id("com.android.application") version "8.11.0" apply false
    id("com.android.library") version "8.11.0" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
    id("org.cyclonedx.bom") version "2.3.1" apply false
}

subprojects {
    apply(plugin = "org.cyclonedx.bom")
}