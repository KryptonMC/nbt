import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.20"
    id("org.cadixdev.licenser") version "0.6.1"
    id("info.solidsoft.pitest") version "1.5.1"
    `maven-publish`
    signing
}

group = "org.kryptonmc"
version = "2.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk7"))
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit5"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.compileKotlin {
    kotlinOptions.freeCompilerArgs = listOf("-Xjvm-default=all", "-Xexplicit-api=strict")
}

tasks.test {
    useJUnitPlatform()
}

task<Jar>("sourcesJar") {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
}

license {
    header.set(project.resources.text.fromFile("HEADER.txt"))
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
            val snapshots = uri("https://repo.kryptonmc.org/snapshots")
            val releases = uri("https://repo.kryptonmc.org/releases")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshots else releases

            credentials {
                username = if (project.hasProperty("maven.username")) project.property("maven.username").toString() else System.getenv("MAVEN_USERNAME")
                password = if (project.hasProperty("maven.password")) project.property("maven.password").toString() else System.getenv("MAVEN_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("nbt") {
            from(components["kotlin"])
            artifact(tasks["sourcesJar"])

            pom {
                name.set("Krypton NBT")
                description.set("A small, easy to use NBT library, designed for use in Krypton, and inspired by vanilla Minecraft")
                url.set("https://kryptonmc.org/nbt")
                inceptionYear.set("2021")
                packaging = "jar"

                developers {
                    developer {
                        id.set("bombardygamer")
                        name.set("Callum Seabrook")
                        email.set("callum.seabrook@prevarinite.com")
                        timezone.set("Europe/London")
                        roles.set(setOf("Lead Developer"))
                    }
                }

                organization {
                    name.set("KryptonMC")
                    url.set("https://github.com/KryptonMC")
                }

                issueManagement {
                    system.set("GitHub")
                    url.set("https://github.com/KryptonMC/nbt/issues")
                }

                scm {
                    connection.set("scm:git:git://github.com/KryptonMC/nbt.git")
                    developerConnection.set("scm:git:ssh://github.com:KryptonMC/nbt.git")
                    url.set("https://github.com/KryptonMC/nbt")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["nbt"])
}
