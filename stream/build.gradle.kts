plugins {
    id("nbt.common")
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                api(project(":nbt-common"))
                api("com.squareup.okio:okio-multiplatform:2.10.0")
            }
        }
        getByName("jvmMain") {
            dependencies {
                api("com.squareup.okio:okio:2.10.0")
            }
        }
    }
}
