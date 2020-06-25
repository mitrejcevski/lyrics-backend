package nl.jovmit.lyrics.domain.users

import nl.jovmit.lyrics.infrastructure.builder.UserBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

abstract class UserRepositoryContract {

    private val mile = UserBuilder.aUser().withUsername("mile").build()
    private val tom = UserBuilder.aUser().withUsername("tom").build()
    private val tomCredentials = UserCredentials(tom.username, tom.password)
    private val incorrectMileCredentials = UserCredentials(mile.username, "wrong")
    private val unknownCredentials = UserCredentials("unknown", "unknown")

    @Test
    fun inform_when_username_is_taken() {
        val repository = repositoryWith(tom)

        assertThat(repository.isUsernameTaken(tom.username)).isTrue()
        assertThat(repository.isUsernameTaken(mile.username)).isFalse()
    }

    @Test
    fun return_user_matching_valid_credentials() {
        val repository = repositoryWith(tom, mile)

        assertThat(repository.userFor(tomCredentials)).isPresent()
        assertThat(repository.userFor(unknownCredentials)).isEmpty()
        assertThat(repository.userFor(incorrectMileCredentials)).isEmpty()
    }

    @Test
    fun inform_when_contains_user_for_given_user_id() {
        val repository = repositoryWith(tom)

        assertThat(repository.hasUserWithId(tom.id)).isTrue()
        assertThat(repository.hasUserWithId(mile.id)).isFalse()
    }

    abstract fun repositoryWith(vararg users: User): UserRepository
}