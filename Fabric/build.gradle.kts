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
    fabric()
}

val minecraftVersion = project.properties["minecraft_version"] as String

configurations {
    create("common")
    create("shadowCommon")
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentFabric").extendsFrom(configurations["common"])
}

loom {
    accessWidenerPath.set(project(":Common").loom.accessWidenerPath)
    runs.create("datagen") {
        server()
        name("Data Generation")
        vmArg("-Dfabric-api.datagen")
        vmArg("-Dfabric-api.datagen.output-dir=${project(":Common").file("src/main/generated/resources").absolutePath}")
        vmArg("-Dfabric-api.datagen.modid=examplemod")

        runDir("build/datagen")
    }
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${project.properties["fabric_loader_version"]}")
    modApi("net.fabricmc.fabric-api:fabric-api:${project.properties["fabric_api_version"]}+$minecraftVersion")

    "common"(project(":Common", "namedElements")) { isTransitive = false }
    "shadowCommon"(project(":Common", "transformProductionFabric")) { isTransitive = false }

    modApi("com.github.glitchfiend:TerraBlender-fabric:${minecraftVersion}-${project.properties["terrablender_version"]}")
    modApi("software.bernie.geckolib:geckolib-fabric-${minecraftVersion}:${project.properties["geckolib_version"]}")
    modApi("maven.modrinth:corgilib:${minecraftVersion}-${project.properties["corgilib_version"]}-fabric")
}

tasks {
    base.archivesName.set(base.archivesName.get() + "-Fabric")
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to project.version))
        }
    }

    shadowJar {
        configurations = listOf(project.configurations.getByName("shadowCommon"))
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        injectAccessWidener.set(true)
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

        val mainFile = upload(project.properties["fabric_curseforge_id"], remapJar)
        mainFile.changelog = dependencyText + changelogText
        mainFile.changelogType = "markdown"
        mainFile.releaseType = project.properties["release_type"]
        mainFile.addRequirement("terrablender-fabric")
        mainFile.addRequirement("geckolib")
        mainFile.addRequirement("corgilib")
        mainFile.addRequirement("fabric-api")
        mainFile.addGameVersion(minecraftVersion)
        mainFile.addModLoader("fabric")
    }

    create("fabric_publishModrinth", TaskModrinthUpload::class) {
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
            loaders.set(mutableListOf("fabric"))
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
        artifactId = "${project.properties["archives_base_name"]}" + "-Fabric"
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