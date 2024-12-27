package si.travelbuddy.dto

data class UpdateUserPasswordDto(
    val id: Long,
    val passwordPlaintext: String,
)