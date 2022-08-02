import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
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
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    api(kotlin("stdlib"))
    api("org.jetbrains.kotlinx", "kotlinx-collections-immutable", "0.3.5")
    testImplementation(kotlin("test-junit5"))
}

license {
    header(project.rootProject.resources.text.fromFile("HEADER.txt"))
    newLine(false)
}

val sourceSets = extensions.getByName("sourceSets") as SourceSetContainer

task<Jar>("sourcesJar") {
    from(sourceSets.named("main").get().allSource)
    archiveClassifier.set("sources")
}

task<Jar>("javadocJar") {
    from(tasks["dokkaJavadoc"])
    archiveClassifier.set("javadoc")
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
    publications.create<MavenPublication>("mavenKotlin") {
        groupId = rootProject.group as String
        artifactId = project.name
        version = rootProject.version as String

        from(components["kotlin"])
        artifact(tasks["sourcesJar"])
        artifact(tasks["javadocJar"])

        pom {
            name.set("Krypton NBT")
            description.set("An advanced library for working with Minecraft's Named Binary Tag format.")
            url.set("https://www.kryptonmc.org")
            inceptionYear.set("2021")
            packaging = "jar"

            developers {
                developer("bombardygamer", "Callum Seabrook", "callum.seabrook@prevarinite.com", "Europe/London", "Developer", "Maintainer")
            }

            organization {
                name.set("KryptonMC")
                url.set("https://www.kryptonmc.org")
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

signing {
    sign(publishing.publications["mavenKotlin"])
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = listOf("-Xjvm-default=all")
        }
    }
    withType<Test> {
        useJUnitPlatform()
    }
}

tasks["build"].dependsOn(tasks["test"])
