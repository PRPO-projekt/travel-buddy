package si.travelbuddy

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import si.travelbuddy.dto.UpdateUserDto
import si.travelbuddy.dto.UpdateUserPasswordDto
import si.travelbuddy.dto.UserDto

class UserService(private val dataBase: Database) {
    init {
        transaction {
            SchemaUtils.create(UserTable)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun create(user: UserDto) : Long = transaction {
        UserDao.new {
            this.username = user.username
            this.name = user.name
            this.surname = user.surname
            this.passwordHash = user.passwordHash
            this.passwordSalt = user.passwordSalt
            this.created = user.created.toInstant()
        }.id.value
    }

    suspend fun updateUser(user: UpdateUserDto) = dbQuery {
        UserTable.update({UserTable.id eq user.id}) {
            if (user.name != null) it[UserTable.name] = user.name
            if (user.surname != null) it[UserTable.surname] = user.surname
            if (user.username != null) it[UserTable.username] = user.username
        }
    }
    // TODO implement hashing with salt
    suspend fun updatePassword(user: UpdateUserPasswordDto) = dbQuery {
        UserTable.update({ UserTable.id eq user.id }) {
            it[UserTable.passwordHash] = user.passwordPlaintext
            it[UserTable.passwordSalt] = user.passwordPlaintext
        }
    }
}