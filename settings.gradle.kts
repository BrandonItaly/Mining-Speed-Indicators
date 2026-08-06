pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.kikugie.dev/releases")
        gradlePluginPortal()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9-beta.2"
}

stonecutter {
    create(rootProject) {
        version("1.21-fabric", "1.21")
        version("1.21-neoforge", "1.21")
        version("26.1-fabric", "26.2")
        version("26.1-neoforge", "26.2")
        vcsVersion = "26.1-fabric"
    }
}

rootProject.name = "Mining Speed Tooltips"
