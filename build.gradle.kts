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
    implementation(kotlin("stdlib-jdk7"))
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit5"))
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "16"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

tasks.test {
    useJUnitPlatform()
}

license {
    header.set(project.resources.text.fromFile("HEADER.txt"))
    newLine.set(false)
}
