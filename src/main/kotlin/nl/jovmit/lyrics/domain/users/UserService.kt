package nl.jovmit.lyrics.domain.users

import nl.jovmit.lyrics.infrastructure.utils.IdGenerator

class UserService(
    private val idGenerator: IdGenerator,
    private val userRepository: UserRepository
) {

    fun createUser(userData: UserData): User {
        validateUsername(userData.username)
        val user = createUserFrom(userData)
        userRepository.add(user)
        return user
    }

    private fun validateUsername(username: String) {
        if (userRepository.isUsernameTaken(username)) {
            throw UsernameAlreadyInUseException()
        }
    }

    private fun createUserFrom(userData: UserData): User {
        val userId = idGenerator.next()
        return with(userData) {
            User(userId, username, password, about)
        }
    }
}
