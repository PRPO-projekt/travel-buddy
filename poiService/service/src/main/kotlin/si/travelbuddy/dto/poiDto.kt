package si.travelbuddy.dto

data class poiDto (
    val id: Int?= null,
    val name: String ?= null,
    val description: String ?= null,
    val lat: Double ?= null,
    val lon: Double ?= null,
    val idPostaje: Int ?= null,
)

