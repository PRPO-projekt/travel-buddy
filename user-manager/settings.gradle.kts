val kotlin_version: String by settings
val ktor_version: String by settings

rootProject.name = "user-manager-service"

include("api")
include("service")
include("entity")