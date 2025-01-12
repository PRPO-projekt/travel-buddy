package si.travelbuddy.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserDto (
    val name: String,
    val surname: String,
    val username: String,
    val password: String,
)