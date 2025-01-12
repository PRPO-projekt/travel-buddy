package si.travelbuddy.dto

import java.time.ZonedDateTime

data class UserDto(
    val id: Long,
    val name: String,
    val surname: String,
    val username: String,
    val passwordHash: String,
    val passwordSalt: String,
    val created: ZonedDateTime,
)