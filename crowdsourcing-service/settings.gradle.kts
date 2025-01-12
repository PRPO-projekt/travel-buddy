val kotlin_version: String by settings
val ktor_version: String by settings

rootProject.name = "crowdsourcing-service"

include("api")
include("service")
include("entity")
