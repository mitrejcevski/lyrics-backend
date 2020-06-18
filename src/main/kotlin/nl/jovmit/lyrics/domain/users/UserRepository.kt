package nl.jovmit.lyrics.domain.users

import java.util.*

class UserRepository {

    private val users = mutableListOf<User>()

    fun add(user: User) {
        users.add(user)
    }

    fun isUsernameTaken(username: String): Boolean {
        return users.any { it.username == username }
    }

    fun userFor(userCredentials: UserCredentials): Optional<User> {
        val user = users.firstOrNull { it.matchesCredentials(userCredentials) }
        return if (user != null) Optional.of(user) else Optional.empty()
    }

    fun hasUserWithId(userId: String): Boolean {
        return users.any { it.id == userId }
    }
}
