
plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.5.2"
}

group = "com.artframework"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2021.2")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild.set("212")
        untilBuild.set("222.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

dependencies {
//    api(fileTree("/libs") { include("*.jar") })
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.postgresql:postgresql:42.5.0")
    implementation ("mysql:mysql-connector-java:8.0.28")
    implementation ("com.baomidou:mybatis-plus-core:3.5.3.1")
    implementation ("com.baomidou:mybatis-plus-generator:3.5.3.1")
    implementation ("com.baomidou:mybatis-plus-annotation:3.5.3.1")
    implementation ("org.slf4j:slf4j-api:2.0.7")
    implementation ("org.freemarker:freemarker:2.3.31")
    implementation ("cn.hutool:hutool-all:5.7.16")
    implementation ("org.apache.commons:commons-lang3:3.12.0")
}
