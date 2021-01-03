tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
        kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
    }

    jar {
        enabled = true
    }
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.9.6")
}

