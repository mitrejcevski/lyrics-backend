package nl.jovmit.lyrics.domain.users

class LoginService(
    private val userRepository: UserRepository
) {

    fun login(userCredentials: UserCredentials): User {
        val user = userRepository.userFor(userCredentials)
        if (user.isPresent) {
            return user.get()
        }
        throw InvalidUserCredentialsException()
    }
}
