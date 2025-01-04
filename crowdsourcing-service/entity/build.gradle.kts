plugins {
    kotlin("jvm") version "2.0.21"
}

group = "si.travelbuddy.crowdsourcing"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}