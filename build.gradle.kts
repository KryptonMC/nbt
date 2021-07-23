plugins {
    kotlin("jvm") version "1.5.20"
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
