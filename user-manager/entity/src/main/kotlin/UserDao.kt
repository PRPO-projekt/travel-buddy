package si.travelbuddy

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object UserTable : LongIdTable() {
    val name = varchar("name", 255)
    val surname = varchar("surname", 255)
    val username = varchar("username", 255)
    val passwordHash = varchar("password_hash", 255)
    val passwordSalt = varchar("password_salt", 255)
    val created = timestamp("created")
}

class UserDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserDao>(UserTable)

    var name by UserTable.name
    var surname by UserTable.surname
    var username by UserTable.username
    var passwordHash by UserTable.passwordHash
    var passwordSalt by UserTable.passwordSalt
    var created by UserTable.created

    fun toModel(): User = User(id.value, name, surname, username, passwordHash, passwordSalt, created.toString())
}

@Serializable
data class User(
    val id: Long,
    val name: String,
    val surname: String,
    val username: String,
    val passwordHash: String,
    val passwordSalt: String,
    val created: String,
)

