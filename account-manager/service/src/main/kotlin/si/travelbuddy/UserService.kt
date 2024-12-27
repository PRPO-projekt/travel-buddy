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
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

private const val ALGORITHM = "PBKDF2WithHmacSHA512"
private const val ITERATIONS = 120_000
private const val KEY_LENGTH = 256
private const val SECRET = "TravelBuddySecret"

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
            if (user.name != null) it[name] = user.name
            if (user.surname != null) it[surname] = user.surname
            if (user.username != null) it[username] = user.username
        }
    }
    // TODO implement hashing with salt
    suspend fun updatePassword(user: UpdateUserPasswordDto) = dbQuery {
        UserTable.update({ UserTable.id eq user.id }) {
            it[passwordHash] = user.passwordPlaintext
            it[passwordSalt] = user.passwordPlaintext
        }
    }

    private fun generateRandomSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt
    }

    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

    fun generateHash(password: String, salt: String): String {
        val combinedSalt = "$salt$SECRET".toByteArray()
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), combinedSalt, ITERATIONS, KEY_LENGTH)
        val key: SecretKey = factory.generateSecret(spec)
        val hash: ByteArray = key.encoded
        return hash.toHexString()
    }
}