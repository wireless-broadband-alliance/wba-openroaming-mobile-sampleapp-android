plugins {
    id("com.android.application") version "9.0.1" apply false
    id("com.android.library") version "9.0.1" apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
    id("com.google.firebase.crashlytics") version "3.0.7" apply false
}

subprojects {

    configurations.all {

        /*
        exclude(group = "javax.annotation", module = "javax.annotation-api")
        exclude(group = "net.java.dev.jna", module = "jna")
        exclude(group = "net.java.dev.jna", module = "jna-platform")
        exclude(group = "net.sf.kxml", module = "kxml2")
        */

        resolutionStrategy {

            force(
                "io.netty:netty-all:4.2.12.Final",
                "io.netty:netty-buffer:4.2.12.Final",
                "io.netty:netty-codec:4.2.12.Final",
                "io.netty:netty-codec-http:4.2.12.Final",
                "io.netty:netty-codec-http2:4.2.12.Final",
                "io.netty:netty-common:4.2.12.Final",
                "io.netty:netty-handler:4.2.12.Final",
                "io.netty:netty-handler-proxy:4.2.12.Final",
                "io.netty:netty-resolver:4.2.12.Final",
                "io.netty:netty-transport:4.2.12.Final",
                "io.netty:netty-transport-native-unix-common:4.2.12.Final",
                "com.google.protobuf:protobuf-java:3.34.1"
            )

            /*
            force(
                "io.netty:netty-buffer:4.2.9.Final",
                "io.netty:netty-codec:4.2.9.Final",
                "io.netty:netty-codec-http:4.2.9.Final",
                "io.netty:netty-codec-http2:4.2.9.Final",
                "io.netty:netty-common:4.2.9.Final",
                "io.netty:netty-handler:4.2.9.Final",
                "io.netty:netty-handler-proxy:4.2.9.Final",
                "io.netty:netty-resolver:4.2.9.Final",
                "io.netty:netty-transport:4.2.9.Final",
                "io.netty:netty-transport-native-unix-common:4.2.9.Final",
                "com.google.protobuf:protobuf-java:4.33.4"
            )

             */
        }
    }
}
