import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    kotlin("multiplatform")
    id("org.cadixdev.licenser")
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
//    js {
//        browser()
//        nodejs()
//    }
}

dependencies {
    "commonMainApi"(kotlin("stdlib-common"))
    "jvmMainApi"(kotlin("stdlib"))
//    "jsMainApi"(kotlin("stdlib-js"))
    "commonTestImplementation"(kotlin("test-common"))
    "commonTestImplementation"(kotlin("test-annotations-common"))
    "jvmTestImplementation"(kotlin("test-junit5"))
//    "jsTestImplementation"(kotlin("test-js"))
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

            credentials {
                username = if (project.hasProperty("maven.username")) project.property("maven.username").toString() else System.getenv("MAVEN_USERNAME")
                password = if (project.hasProperty("maven.password")) project.property("maven.password").toString() else System.getenv("MAVEN_PASSWORD")
            }
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
