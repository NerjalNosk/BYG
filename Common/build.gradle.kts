import com.matyrobbrt.gradle.pngoptimiser.task.OptimisePNGTask

architectury {
    common("forge", "fabric")
    platformSetupLoomIde()
}

plugins {
    id("com.matyrobbrt.pngoptimiser") version "0.2.0"
}

val minecraftVersion = project.properties["minecraft_version"] as String

loom.accessWidenerPath.set(file("src/main/resources/byg.accesswidener"))

sourceSets.main.get().resources.srcDir("src/main/generated/resources")

dependencies {
    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation("net.fabricmc:fabric-loader:${project.properties["fabric_loader_version"]}")

    compileOnly("org.ow2.asm:asm-tree:9.5")
    compileOnly("software.bernie.geckolib:geckolib-forge-${minecraftVersion}:${project.properties["geckolib_version"]}")
    compileOnly("maven.modrinth:corgilib:${minecraftVersion}-${project.properties["corgilib_version"]}-forge")
    compileOnly("com.github.glitchfiend:TerraBlender-common:${minecraftVersion}-${project.properties["terrablender_version"]}")
}

tasks.register("optimise", OptimisePNGTask::class) {
    option("i", 0)
}

tasks.register("prepareWorkspace")

/*
publishing {
    publications.create<MavenPublication>("mavenCommon") {
        artifactId = "${project.properties["archives_base_name"]}" + "-Common"
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
