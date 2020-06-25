package nl.jovmit.lyrics.domain.users

import org.junit.jupiter.api.AfterEach
import java.io.File

class FileBasedUserRepositoryShould : UserRepositoryContract() {

    private val repository = FileBasedUserRepository()

    override fun repositoryWith(vararg users: User): UserRepository {
        users.forEach {
            repository.add(it)
        }
        return repository
    }

    @AfterEach
    fun tearDown() {
        File("files/users").deleteRecursively()
    }
}