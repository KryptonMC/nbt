import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    kotlin("jvm")
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
}

dependencies {
    api(kotlin("stdlib"))
    api("org.jetbrains.kotlinx", "kotlinx-collections-immutable", "0.3.5")
    testImplementation(kotlin("test-junit5"))
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
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    withType<Test> {
        useJUnitPlatform()
    }
}
