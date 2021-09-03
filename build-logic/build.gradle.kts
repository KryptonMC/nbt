plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.5.30")
    implementation("org.jetbrains.dokka", "dokka-gradle-plugin", "1.4.30")
    implementation("gradle.plugin.org.cadixdev.gradle", "licenser", "0.6.1")
    implementation("info.solidsoft.gradle.pitest", "gradle-pitest-plugin", "1.6.0")
}
