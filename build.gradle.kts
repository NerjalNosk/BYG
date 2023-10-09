import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "1.3-SNAPSHOT" apply false
    id("com.matyrobbrt.mc.registrationutils") version "1.20.1-1.0.0"
    idea
    java
}

registrationUtils {
    group("potionstudios.byg.reg")
    projects {
        register("Common") { type("common") }
        register("Fabric") { type("fabric")}
        register("Forge") { type("forge") }
    }
}


val minecraftVersion = project.properties["minecraft_version"] as String

architectury.minecraft = minecraftVersion

subprojects {
    apply(plugin = "dev.architectury.loom")

    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://maven.parchmentmc.org")
        maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
        maven("https://api.modrinth.com/maven").content { includeGroup("maven.modrinth") }
        maven("https://jitpack.io")
    }

    @Suppress("UnstableApiUsage")
    dependencies {
        "minecraft"("com.mojang:minecraft:$minecraftVersion")
        "mappings"(loom.layered{
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-$minecraftVersion:${project.properties["parchment"]}@zip")
        })
        compileOnly("com.google.auto.service:auto-service:1.1.1")
        annotationProcessor("com.google.auto.service:auto-service:1.1.1")
        compileOnly("org.jetbrains:annotations:24.0.1")
    }
    loom.silentMojangMappingsLicense()
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")
    apply(plugin = "idea")

    version = project.properties["version"] as String
    group = project.properties["group"] as String
    base.archivesName.set(project.properties["archives_base_name"] as String)

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    java.withSourcesJar()
}
