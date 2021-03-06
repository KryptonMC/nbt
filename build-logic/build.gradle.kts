plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.6.0")
    implementation("org.jetbrains.dokka", "dokka-gradle-plugin", "1.6.0")
    implementation("org.jetbrains.kotlinx", "binary-compatibility-validator", "0.8.0")
    implementation("gradle.plugin.org.cadixdev.gradle", "licenser", "0.6.1")
    implementation("info.solidsoft.gradle.pitest", "gradle-pitest-plugin", "1.6.0")
}
