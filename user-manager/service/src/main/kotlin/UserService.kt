package si.travelbuddy

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.update
import si.travelbuddy.dto.UpdateUserDto

class UserService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(UserTable)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun updateUser(userData: UpdateUserDto) = dbQuery {
        val user = UserDao.findByIdAndUpdate(userData.id)
        {
            if (userData.name != null) it.name = userData.name
            if (userData.surname != null) it.surname = userData.surname
            if (userData.username != null) it.username = userData.username
        }
    }

    suspend fun deleteUser(userId: Long) = dbQuery {
        UserDao.findById(userId)?.delete()
    }

    suspend fun getUserById(id: Long) = dbQuery {
        UserDao.findById(id)
    }



}
