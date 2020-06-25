package nl.jovmit.lyrics.domain.users

import java.util.*

class InMemoryUserRepository : UserRepository {

    private val users = mutableListOf<User>()

    override fun add(user: User) {
        users.add(user)
    }

    override fun isUsernameTaken(username: String): Boolean {
        return users.any { it.username == username }
    }

    override fun userFor(userCredentials: UserCredentials): Optional<User> {
        val user = users.firstOrNull { it.matchesCredentials(userCredentials) }
        return if (user != null) Optional.of(user) else Optional.empty()
    }

    override fun hasUserWithId(userId: String): Boolean {
        return users.any { it.id == userId }
    }
}
