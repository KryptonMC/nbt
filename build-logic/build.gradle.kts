plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.7.10")
    implementation("org.jetbrains.dokka", "dokka-gradle-plugin", "1.7.10")
    implementation("org.jetbrains.kotlinx", "binary-compatibility-validator", "0.11.0")
    implementation("gradle.plugin.org.cadixdev.gradle", "licenser", "0.6.1")
    implementation("info.solidsoft.gradle.pitest", "gradle-pitest-plugin", "1.7.4")
}

tasks.compileJava {
    options.release.set(8)
}
