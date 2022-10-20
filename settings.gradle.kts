pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "nbt"

sequenceOf("common", "extra-kotlin", "stream").forEach {
    include("nbt-$it")
    project(":nbt-$it").projectDir = file(it)
}
