plugins {
    val kotlinVersion = "1.4.10"
    kotlin("jvm") version kotlinVersion apply false
}

allprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    group = "com.loca-love"
    version = "0.0.1"
    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
    }

    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}