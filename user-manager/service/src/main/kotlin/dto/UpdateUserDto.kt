package si.travelbuddy.dto

data class UpdateUserDto(
    val id: Long,
    val name: String? = null,
    val surname: String? = null,
    val username: String? = null,
)