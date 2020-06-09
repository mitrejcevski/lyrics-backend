package nl.jovmit.lyrics.domain.users

import nl.jovmit.lyrics.infrastructure.builder.UserBuilder.Companion.aUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserRepositoryShould {

    private val mile = aUser().withUsername("mile").build()
    private val tom = aUser().withUsername("tom").build()

    private val repository = UserRepository()

    @Test
    fun inform_when_username_is_taken() {
        repository.add(tom)

        assertThat(repository.isUsernameTaken(tom.username)).isTrue()
        assertThat(repository.isUsernameTaken(mile.username)).isFalse()
    }
}