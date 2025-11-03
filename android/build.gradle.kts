plugins {
    id("com.android.application") version "8.13.0" apply false
    id("com.android.library") version "8.13.0" apply false
    id("org.jetbrains.kotlin.android") version "2.2.21" apply false
}

subprojects {

    configurations.all {

        exclude(group = "com.google.protobuf", module = "protobuf-java")
        exclude(group = "javax.annotation", module = "javax.annotation-api")
        exclude(group = "net.java.dev.jna", module = "jna")
        exclude(group = "net.java.dev.jna", module = "jna-platform")
        exclude(group = "net.sf.kxml", module = "kxml2")

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
                "io.netty:netty-transport-native-unix-common:4.2.7.Final",
                "com.google.protobuf:protobuf-java:4.33.0"
            )
        }
    }
}