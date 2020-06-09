package nl.jovmit.lyrics.domain.users

import nl.jovmit.lyrics.infrastructure.builder.UserBuilder.Companion.aUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class LoginServiceShould {

    @Mock
    private lateinit var userRepository: UserRepository

    private val user = aUser().build()
    private val userCredentials = UserCredentials(user.username, user.password)

    private lateinit var loginService: LoginService

    @BeforeEach
    fun setUp() {
        loginService = LoginService(userRepository)
    }

    @Test
    fun perform_login() {
        given(userRepository.userFor(userCredentials)).willReturn(Optional.of(user))

        val result = loginService.login(userCredentials)

        assertThat(result).isEqualTo(user)
    }

    @Test
    fun throw_exception_when_logging_in_with_wrong_credentials() {
        given(userRepository.userFor(userCredentials)).willReturn(Optional.empty())

        assertThrows<InvalidUserCredentialsException> {
            loginService.login(userCredentials)
        }
    }
}