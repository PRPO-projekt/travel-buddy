
pluginManagement {
    plugins {
        val kotlin_version: String by settings
        val ktor_version: String by settings

        kotlin("jvm") version kotlin_version
        id("io.ktor.plugin") version ktor_version
        id("org.jetbrains.kotlin.plugin.serialization") version kotlin_version
    }
}

rootProject.name = "crowdsourcing-service"

include("api")
include("service")
include("entity")
