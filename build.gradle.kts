plugins {
    kotlin("jvm") version "1.5.20"
    id("org.cadixdev.licenser") version "0.6.1"
    `maven-publish`
    signing
}

group = "org.kryptonmc"
version = "1.4"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk7"))
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit5"))
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "16"
        freeCompilerArgs = listOf("-Xjvm-default=all", "-Xexplicit-api=strict")
    }
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
