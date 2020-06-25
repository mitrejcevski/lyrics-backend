package nl.jovmit.lyrics.domain.users

import nl.jovmit.lyrics.infrastructure.utils.IdGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceShould {

    @Mock
    private lateinit var idGenerator: IdGenerator

    @Mock
    private lateinit var userRepository: UserRepository

    private val userId = UUID.randomUUID().toString()
    private val password = "password"
    private val username = "username"
    private val about = "about"
    private val userData = UserData(username, password, about)
    private val user = User(userId, username, password, about)

    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userService = UserService(idGenerator, userRepository)
    }

    @Test
    fun create_a_new_user() {
        given(idGenerator.next()).willReturn(userId)

        val result = userService.createUser(userData)

        verify(userRepository).add(user)
        assertThat(user).isEqualTo(result)
    }

    @Test
    fun throw_an_exception_when_creating_a_duplicate_user() {
        given(userRepository.isUsernameTaken(username)).willReturn(true)

        assertThrows<UsernameAlreadyInUseException> {
            userService.createUser(userData)
        }
    }
}