package nl.jovmit.lyrics.domain.users

import java.util.*

interface UserRepository {

    fun add(user: User)

    fun isUsernameTaken(username: String): Boolean

    fun userFor(userCredentials: UserCredentials): Optional<User>

    fun hasUserWithId(userId: String): Boolean
}