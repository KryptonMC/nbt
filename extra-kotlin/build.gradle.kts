plugins {
    kotlin("jvm")
    id("nbt.common")
}

dependencies {
    api(project(":nbt-common"))
    api(kotlin("stdlib"))
}

kotlin {
    explicitApi()
}
