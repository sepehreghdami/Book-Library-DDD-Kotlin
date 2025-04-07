plugins {
    kotlin("jvm") version "1.8.0"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

//application {
//    mainClass.set("com.example.library.MainKt")
//}

// ✅ JVM Toolchain block to ensure Kotlin + Java match
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // or 11 or 8
    }
}
