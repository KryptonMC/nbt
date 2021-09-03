import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get

fun PublishingExtension.configurePublication(project: Project, id: String) {
    publications {
        create<MavenPublication>("mavenKotlin") {
            artifactId = id

            from(project.components["kotlin"])
            artifact(project.tasks["sourcesJar"])

            pom {
                name.set("Krypton NBT")
                description.set("An advanced NBT library, based around vanilla Minecraft, with ideas from JSON libraries incorporated.")
                url.set("https://kryptonmc.org/projects/nbt")
                inceptionYear.set("2021")
                packaging = "jar"

                developers {
                    developer {
                        this.id.set("bombardygamer")
                        name.set("Callum Seabrook")
                        email.set("callum.seabrook@prevarinite.com")
                        timezone.set("Europe/London")
                        roles.set(setOf("Lead Developer"))
                    }
                }

                organization {
                    name.set("KryptonMC")
                    url.set("https://github.com/KryptonMC")
                }

                issueManagement {
                    system.set("GitHub")
                    url.set("https://github.com/KryptonMC/nbt/issues")
                }

                scm {
                    connection.set("scm:git:git://github.com/KryptonMC/nbt.git")
                    developerConnection.set("scm:git:ssh://github.com:KryptonMC/nbt.git")
                    url.set("https://github.com/KryptonMC/nbt")
                }
            }
        }
    }
}
