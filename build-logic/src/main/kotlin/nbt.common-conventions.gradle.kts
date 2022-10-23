plugins {
    id("net.kyori.indra")
    id("net.kyori.indra.publishing")
    id("org.cadixdev.licenser")
    // We need to include these on all projects because of a bug in Gradle. If the projects have differing plugins, the build
    // will fail due to a build cache issue. See https://github.com/gradle/gradle/issues/17559
    id("org.gradlex.extra-java-module-info")
    kotlin("jvm")
}

val libs = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class)

dependencies {
    compileOnly(libs.annotations)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}

indra {
    javaVersions {
        target(17)
    }

    github("KryptonMC", "nbt")
    mitLicense()

    publishReleasesTo("krypton", "https://repo.kryptonmc.org/releases")
    publishSnapshotsTo("krypton", "https://repo.kryptonmc.org/snapshots")
    configurePublications {
        artifactId = "nbt-${project.name}"
        pom {
            name.set("Krypton NBT")
            description.set("An advanced library for working with Minecraft's Named Binary Tag format.")
            url.set("https://www.kryptonmc.org")
            inceptionYear.set("2021")

            developers {
                developer("bombardygamer", "Callum Seabrook", "callum.seabrook@prevarinite.com", "Europe/London", "Developer", "Maintainer")
            }

            organization {
                name.set("KryptonMC")
                url.set("https://www.kryptonmc.org")
            }
        }
    }
}

license {
    header(project.rootProject.resources.text.fromFile("HEADER.txt"))
    newLine(false)
}

extraJavaModuleInfo {
    failOnMissingModuleInfo.set(false)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
}

tasks["build"].dependsOn(tasks["test"])
