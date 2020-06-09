package nl.jovmit.lyrics.domain.users

import nl.jovmit.lyrics.infrastructure.builder.UserBuilder.Companion.aUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserRepositoryShould {

    private val mile = aUser().withUsername("mile").build()
    private val tom = aUser().withUsername("tom").build()
    private val tomCredentials = UserCredentials(tom.username, tom.password)
    private val incorrectMileCredentials = UserCredentials(mile.username, "wrong")
    private val unknownCredentials = UserCredentials("unknown", "unknown")

    private val repository = UserRepository()

    @Test
    fun inform_when_username_is_taken() {
        repository.add(tom)

        assertThat(repository.isUsernameTaken(tom.username)).isTrue()
        assertThat(repository.isUsernameTaken(mile.username)).isFalse()
    }

    @Test
    fun return_user_matching_valid_credentials() {
        repository.add(tom)
        repository.add(mile)

        assertThat(repository.userFor(tomCredentials)).isPresent()
        assertThat(repository.userFor(unknownCredentials)).isEmpty()
        assertThat(repository.userFor(incorrectMileCredentials)).isEmpty()
    }
}