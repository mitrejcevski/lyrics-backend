package nl.jovmit.lyrics.infrastructure.builder

import nl.jovmit.lyrics.domain.users.User
import java.util.*

class UserBuilder {

    companion object {
        fun aUser(): UserBuilder {
            return UserBuilder()
        }
    }

    private var userId = UUID.randomUUID().toString()
    private var username = "username"
    private var password = "password"
    private var about = "about"

    fun withId(id: String): UserBuilder {
        this.userId = id
        return this
    }

    fun withUsername(username: String): UserBuilder {
        this.username = username
        return this
    }

    fun withPassword(password: String): UserBuilder {
        this.password = password
        return this
    }

    fun withAbout(about: String): UserBuilder {
        this.about = about
        return this
    }

    fun build(): User {
        return User(userId, username, password, about)
    }
}