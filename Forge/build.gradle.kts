import com.modrinth.minotaur.TaskModrinthUpload
import net.darkhax.curseforgegradle.TaskPublishCurseForge
import java.nio.charset.Charset
import kotlin.io.path.readText

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.darkhax.curseforgegradle") version "1.1.16"
    id("com.modrinth.minotaur") version "2.+"
}

architectury {
    platformSetupLoomIde()
    forge()
}

val minecraftVersion = project.properties["minecraft_version"] as String

configurations {
    create("common")
    create("shadowCommon")
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentForge").extendsFrom(configurations["common"])
}

loom {
    accessWidenerPath.set(project(":Common").loom.accessWidenerPath)

    forge {
        convertAccessWideners.set(true)
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)

        mixinConfig("byg.mixins.json")
        mixinConfig("byg_forge.mixins.json")
    }

    runs.create("data") {
        data()
        programArgs("--all", "--mod", "byg")
        programArgs("--output", project(":Common").file("src/main/generated/resources").absolutePath)
        programArgs("--existing", project(":Common").file("src/main/resources").absolutePath)
    }
}

dependencies {
    forge("net.minecraftforge:forge:$minecraftVersion-${project.properties["forge_version"]}")

    "common"(project(":Common", "namedElements")) { isTransitive = false }
    "shadowCommon"(project(":Common", "transformProductionForge")) { isTransitive = false }

    modApi("com.github.glitchfiend:TerraBlender-forge:${minecraftVersion}-${project.properties["terrablender_version"]}")
    modApi("software.bernie.geckolib:geckolib-forge-$minecraftVersion:${project.properties["geckolib_version"]}")
    modApi("maven.modrinth:corgilib:$minecraftVersion-${project.properties["corgilib_version"]}-forge")
}

tasks {
    base.archivesName.set(base.archivesName.get() + "-Forge")
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand(mapOf("version" to project.version))
        }
    }

    shadowJar {
        exclude("fabric.mod.json")
        configurations = listOf(project.configurations.getByName("shadowCommon"))
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
    }

    jar.get().archiveClassifier.set("dev")

    sourcesJar {
        val commonSources = project(":Common").tasks.sourcesJar
        dependsOn(commonSources)
        from(commonSources.get().archiveFile.map { zipTree(it) })
    }

    val changelogText = projectDir.toPath().parent.resolve("CHANGELOG.md").readText(Charset.defaultCharset())

    val dependencyText = ""

    create("fabic_publishCurseForge", TaskPublishCurseForge::class) {
        dependsOn(build)
        apiToken = System.getenv()["CURSEFORGE_TOKEN"]

        val mainFile = upload(project.properties["forge_curseforge_id"], remapJar)
        mainFile.changelog = dependencyText + changelogText
        mainFile.changelogType = "markdown"
        mainFile.releaseType = project.properties["release_type"]
        mainFile.addRequirement("terrablender-forge")
        mainFile.addRequirement("geckolib")
        mainFile.addRequirement("corgilib")
        mainFile.addGameVersion(minecraftVersion)
        mainFile.addModLoader("forge")
    }

    create("forge_publishModrinth", TaskModrinthUpload::class) {
        dependsOn(build)
        modrinth {
            token.set(System.getenv()["MODRINTH_KEY"])
            projectId.set(project.properties["modrinth_id"] as String)
            versionName.set(base.archivesName.get())
            versionNumber.set(project.properties["version"] as String)
            versionType.set(project.properties["release_type"] as String)
            uploadFile.set(shadowJar)

            val modrinthChangelogText = dependencyText + changelogText.substring(0, changelogText.indexOf("# 2", changelogText.indexOf("# 2") + 1)).trim()
            changelog.set(modrinthChangelogText)

            gameVersions.set(mutableListOf(minecraftVersion))
            loaders.set(mutableListOf("forge"))
        }
    }

}

components {
    java.run {
        if (this is AdhocComponentWithVariants)
            withVariantsFromConfiguration(project.configurations.shadowRuntimeElements.get()) { skip() }
    }
}

/*
publishing {
    publications.create<MavenPublication>("mavenCommon") {
        artifactId = "${project.properties["archives_base_name"]}" + "-Forge"
        from(components["java"])
    }

    repositories {
        mavenLocal()
        maven {
            val releasesRepoUrl = "https://example.com/releases"
            val snapshotsRepoUrl = "https://example.com/snapshots"
            url = uri(if (project.version.toString().endsWith("SNAPSHOT") || project.version.toString().startsWith("0")) snapshotsRepoUrl else releasesRepoUrl)
            name = "ExampleRepo"
            credentials {
                username = project.properties["repoLogin"]?.toString()
                password = project.properties["repoPassword"]?.toString()
            }
        }
    }
}*/