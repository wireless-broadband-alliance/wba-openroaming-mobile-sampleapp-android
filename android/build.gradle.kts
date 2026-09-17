plugins {
    id("com.android.application") version "9.4.0" apply false
    id("com.android.library") version "9.4.0" apply false

    id("com.google.gms.google-services") version "4.5.0" apply false
    id("com.google.firebase.crashlytics") version "3.0.8" apply false

    id("org.cyclonedx.bom") version "3.4.1"
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
                "io.netty:netty-all:4.2.17.Final",
                "io.netty:netty-buffer:4.2.17.Final",
                "io.netty:netty-codec:4.2.17.Final",
                "io.netty:netty-codec-http:4.2.17.Final",
                "io.netty:netty-codec-http2:4.2.17.Final",
                "io.netty:netty-common:4.2.17.Final",
                "io.netty:netty-handler:4.2.17.Final",
                "io.netty:netty-handler-proxy:4.2.17.Final",
                "io.netty:netty-resolver:4.2.17.Final",
                "io.netty:netty-transport:4.2.17.Final",
                "io.netty:netty-transport-native-unix-common:4.2.17.Final",
                "com.google.protobuf:protobuf-java:4.35.0",
                "com.google.protobuf:protobuf-javalite:4.35.0",
                "com.google.guava:guava:33.6.0-android",
                "org.bouncycastle:bcprov-jdk18on:1.84",
                "org.bouncycastle:bcprov-jdk15on:1.84",
                "org.bouncycastle:bcpkix-jdk18on:1.84"
            )

            eachDependency {
                if (requested.group == "com.google.protobuf") useVersion("4.35.0")
            }
        }
    }
}