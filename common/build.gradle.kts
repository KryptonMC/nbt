plugins {
    id("nbt.common")
    id("nbt.templates")
    id("org.gradlex.extra-java-module-info")
}

val arraysData = file("src/templateData/arrays.yaml")
val valuesData = file("src/templateData/values.yaml")
val compoundListData = file("src/templateData/compoundList.yaml")
val licenseText = objects.property<String>()
licenseText.set(provider {
    val lineEnding = license.lineEnding.get()
    rootProject.file("HEADER.txt").readText().trim().split("\r?\n".toRegex())
        .joinToString(lineEnding, "/*$lineEnding", "$lineEnding */") { if (it.isEmpty()) " *" else " * $it" }
})
licenseText.finalizeValueOnRead()

sourceSets {
    main {
        templates.templateSets {
            register("values") {
                dataFiles.from(valuesData)
                variants("byte", "short", "int", "long", "float", "double")
            }
            register("arrays") {
                dataFiles.from(arraysData)
                variants("byte", "int", "long")
            }
            register("compoundList") {
                dataFiles.from(compoundListData)
            }
            register("arraysCommon") {
                dataFiles.from(arraysData)
            }
        }
    }
    configureEach {
        templates.templateSets.configureEach { header.set(licenseText) }
    }
}

dependencies {
    api("org.pcollections", "pcollections", "4.0.0")
}

extraJavaModuleInfo {
    module("pcollections-4.0.0.jar", "org.pcollections", "4.0.0") {
        exports("org.pcollections")
    }
}

tasks.javadoc {
    options {
        this as StandardJavadocDocletOptions
        tags("apiNote:a:API Note:", "implSpec:a:Implementation Requirements:", "implNote:a:Implementation Note:")
    }
}
