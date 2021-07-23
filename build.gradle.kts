plugins {
    kotlin("jvm") version "1.5.20"
    id("org.cadixdev.licenser") version "0.6.1"
}

group = "org.kryptonmc"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "16"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

license {
    header.set(project.resources.text.fromFile("HEADER.txt"))
    newLine.set(false)
}
