package si.travelbuddy

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import si.travelbuddy.dto.RegisterUserDto
import si.travelbuddy.dto.UpdateUserDto
import si.travelbuddy.dto.UpdateUserPasswordDto
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import si.travelbuddy.dto.LoginUserDto
import si.travelbuddy.dto.UserDto
import java.time.ZoneOffset

// TODO: change into environment variables
private const val ALGORITHM = "PBKDF2WithHmacSHA512"
private const val ITERATIONS = 120_000
private const val KEY_LENGTH = 256
private const val SECRET = "TravelBuddySecret"

class AuthService(private val dataBase: Database) {
    init {
        transaction {
            SchemaUtils.create(UserTable)
        }

    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    public suspend fun register(user: RegisterUserDto): User? = transaction {
        val test = UserDao.find { UserTable.username eq user.username }.singleOrNull()
        if (test == null) {
            var salt = generateRandomSalt().toHexString()
            return@transaction UserDao.new {
                this.username = user.username
                this.name = user.name
                this.surname = user.surname
                this.passwordHash = generateHash(user.password, salt)
                this.passwordSalt = salt
                this.created = java.time.ZonedDateTime.now(ZoneOffset.UTC).toInstant()
            }.toModel()
        }
        return@transaction null
    }

    public suspend fun login(loginUser: LoginUserDto): User? = dbQuery {
        val user = UserDao.find { UserTable.username eq loginUser.username }.singleOrNull()
        if (user != null) {
            val passwordHash = generateHash(loginUser.password, user.passwordSalt)
            if (passwordHash == user.passwordHash) {
                return@dbQuery user.toModel()
            }
        }
        return@dbQuery null
    }

    suspend fun updatePassword(user: UpdateUserPasswordDto) = dbQuery {
        var salt = generateRandomSalt().toHexString()
        UserTable.update({ UserTable.id eq user.id }) {
            it[passwordHash] = generateHash(user.password, salt)
            it[passwordSalt] = salt
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

    private fun generateHash(password: String, salt: String): String {
        val combinedSalt = "$salt$SECRET".toByteArray()
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), combinedSalt, ITERATIONS, KEY_LENGTH)
        val key: SecretKey = factory.generateSecret(spec)
        val hash: ByteArray = key.encoded
        return hash.toHexString()
    }
}