package si.travelbuddy.dto

import java.time.ZonedDateTime

data class CreateUserDto (
    val id: Long,
    val name: String,
    val surname: String,
    val username: String,
    val password: String,
    val created: ZonedDateTime,
    )