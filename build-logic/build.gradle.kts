plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.7.10")
    implementation("gradle.plugin.org.cadixdev.gradle", "licenser", "0.6.1")
    implementation("info.solidsoft.gradle.pitest", "gradle-pitest-plugin", "1.7.4")
    implementation("org.gradlex", "extra-java-module-info", "1.0")
    implementation("io.pebbletemplates", "pebble", "3.1.6")
    implementation("net.kyori", "mammoth", "1.2.0")
    implementation("org.snakeyaml", "snakeyaml-engine", "2.4")
}

gradlePlugin {
    plugins {
        create("template") {
            id = "nbt.templates"
            implementationClass = "org.kryptonmc.nbt.templates.TemplateGeneratorPlugin"
        }
    }
}

tasks.compileJava {
    options.release.set(8)
}
