import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    kotlin("multiplatform")
    id("org.cadixdev.licenser")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    jvm {
        withJava()
    }
    js {
        nodejs()
    }
    sourceSets.all {
        languageSettings.optIn("okio.ExperimentalFileSystem")
    }
}

dependencies {
    "commonMainApi"(kotlin("stdlib-common"))
    "commonMainApi"("org.jetbrains.kotlinx", "kotlinx-collections-immutable", "0.3.5")
    "commonMainApi"("com.squareup.okio", "okio-multiplatform", "3.0.0-alpha.9")
    "jvmMainApi"(kotlin("stdlib"))
    "jvmMainApi"("com.squareup.okio", "okio", "3.0.0-alpha.9")
    "jsMainApi"(kotlin("stdlib-js"))
    "jsMainApi"("com.squareup.okio", "okio-nodefilesystem-js", "3.0.0-alpha.9")
    "jsMainApi"(npm("pako", "2.0.3"))
    "commonTestImplementation"(kotlin("test-common"))
    "commonTestImplementation"(kotlin("test-annotations-common"))
    "jvmTestImplementation"(kotlin("test-junit5"))
    "jsTestImplementation"(kotlin("test-js"))
}

license {
    header.set(project.rootProject.resources.text.fromFile("HEADER.txt"))
    newLine.set(false)
}

publishing {
    repositories {
        maven {
            val releases = URI("https://repo.kryptonmc.org/releases")
            val snapshots = URI("https://repo.kryptonmc.org/snapshots")
            url = if (project.version.toString().endsWith("SNAPSHOT")) snapshots else releases
            credentials(PasswordCredentials::class)
        }
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
    withType<Test> {
        useJUnitPlatform()
    }
}
