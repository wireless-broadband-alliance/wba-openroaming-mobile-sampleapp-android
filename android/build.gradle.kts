plugins {
    id("com.android.application") version "8.13.0" apply false
    id("com.android.library") version "8.13.0" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
}

subprojects {

    configurations.all {

        resolutionStrategy {

            force(
                "io.netty:netty-buffer:4.2.7.Final",
                "io.netty:netty-codec:4.2.7.Final",
                "io.netty:netty-codec-http:4.2.7.Final",
                "io.netty:netty-codec-http2:4.2.7.Final",
                "io.netty:netty-common:4.2.7.Final",
                "io.netty:netty-handler:4.2.7.Final",
                "io.netty:netty-handler-proxy:4.2.7.Final",
                "io.netty:netty-resolver:4.2.7.Final",
                "io.netty:netty-transport:4.2.7.Final",
                "io.netty:netty-transport-native-unix-common:4.2.7.Final"
            )
        }
    }
}