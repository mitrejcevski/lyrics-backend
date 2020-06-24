package nl.jovmit.lyrics.domain.users

import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonObject
import java.io.File
import java.util.*

class FileBasedUserRepository : UserRepository {

    private companion object {
        private const val USERS_DIR = "files/users"
        private const val USERS_FILE_NAME = "users"
    }

    private val usersRoot: File = File(USERS_DIR).also { it.mkdirs() }

    override fun add(user: User) {
        val users = readUsers()
        users.add(user)
        writeUsers(users)
    }

    override fun isUsernameTaken(username: String): Boolean {
        val users = readUsers()
        return users.any { it.username == username }
    }

    override fun userFor(userCredentials: UserCredentials): Optional<User> {
        val users = readUsers()
        val match = users.firstOrNull { it.matchesCredentials(userCredentials) }
        return if (match != null) Optional.of(match) else Optional.empty()
    }

    override fun hasUserWithId(userId: String): Boolean {
        val users = readUsers()
        return users.any { it.id == userId }
    }

    private fun readUsers(): MutableList<User> {
        val file = File(usersRoot, USERS_FILE_NAME)
        return if (!file.exists()) {
            mutableListOf()
        } else {
            val jsonArray = JsonArray.readFrom(file.readText())
            jsonArray.map { jsonUser -> userFrom(jsonUser.asObject()) }.toMutableList()
        }
    }

    private fun writeUsers(users: List<User>) {
        val jsonArray = JsonArray()
        users.forEach { user -> jsonArray.add(jsonFor(user)) }
        val file = File(usersRoot, USERS_FILE_NAME)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(jsonArray.toString())
    }

    private fun userFrom(jsonUser: JsonObject): User {
        return User(
            jsonUser.get("id").asString(),
            jsonUser.get("username").asString(),
            jsonUser.get("password").asString(),
            jsonUser.get("about").asString()
        )
    }

    private fun jsonFor(user: User): JsonObject {
        return JsonObject()
            .add("id", user.id)
            .add("username", user.username)
            .add("password", user.password)
            .add("about", user.about)
    }
}
