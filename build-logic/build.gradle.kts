plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

dependencies {
    implementation(libs.plugin.indra)
    implementation(libs.plugin.licenser)
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.extraModuleInfo)
    implementation(libs.mammoth)
    implementation(libs.pebble)
    implementation(libs.snakeyaml)
}

dependencies {
    compileOnly(files(libs::class.java.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        create("template") {
            id = "nbt.templates"
            implementationClass = "org.kryptonmc.nbt.templates.TemplateGeneratorPlugin"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
