package si.travelbuddy.dto

data class UpdateUserPasswordDto(
    val id: Long,
    val password: String,
)