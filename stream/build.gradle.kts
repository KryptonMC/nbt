plugins {
    id("nbt.common")
}

dependencies {
    api(project(":common"))
    api("com.squareup.okio", "okio", "2.10.0")
}

publishing {
    publications {
        configurePublication(project, "nbt-stream")
    }
}
