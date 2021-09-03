import java.net.URI

plugins {
    kotlin("jvm")
    id("org.cadixdev.licenser")
    id("info.solidsoft.pitest")
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit5"))
}

kotlin {
    explicitApi()
}

license {
    header.set(project.rootProject.resources.text.fromFile("HEADER.txt"))
    newLine.set(false)
}

pitest {
    targetClasses.set(setOf("org.kryptonmc.nbt.*"))
    pitestVersion.set("1.7.0")
    outputFormats.set(setOf("HTML", "XML"))
    junit5PluginVersion.set("0.12")
    excludedMethods.set(setOf(
        "equals", "hashCode", "toString", // Standard stuff that is mostly auto-generated
        "contains", "indexOf", "lastIndexOf", "remove", // Collection stuff that we don't need to test
    ))
    avoidCallsTo.set(setOf("kotlin.jvm.internal"))
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

task<Jar>("sourcesJar") {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    test {
        useJUnitPlatform()
    }
}
