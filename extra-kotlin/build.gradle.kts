plugins {
    id("nbt.common-conventions")
    id("nbt.templates")
}

kotlin {
    explicitApi()
}

dependencies {
    api(projects.common)
    api(libs.kotlin.stdlib)
}
