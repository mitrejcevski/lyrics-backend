package nl.jovmit.lyrics.domain.users

import nl.jovmit.lyrics.infrastructure.utils.IdGenerator

class UserService(
    private val idGenerator: IdGenerator,
    private val userRepository: UserRepository
) {

    fun createUser(userData: UserData): User {
        if(userRepository.isUsernameTaken(userData.username)) {
            throw UsernameAlreadyInUseException()
        }
        val userId = idGenerator.next()
        val user = with(userData) {
            User(userId, username, password, about)
        }
        userRepository.add(user)
        return user
    }
}
