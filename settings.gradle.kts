pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "nbt"

sequenceOf("common", "stream").forEach {
    include("nbt-$it")
    project(":nbt-$it").projectDir = file(it)
}
