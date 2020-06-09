package nl.jovmit.lyrics.api

import com.eclipsesource.json.JsonObject
import nl.jovmit.lyrics.domain.users.InvalidUserCredentialsException
import nl.jovmit.lyrics.domain.users.LoginService
import nl.jovmit.lyrics.domain.users.User
import nl.jovmit.lyrics.domain.users.UserCredentials
import nl.jovmit.lyrics.infrastructure.builder.UserBuilder.Companion.aUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import spark.Request
import spark.Response

@ExtendWith(MockitoExtension::class)
class LoginApiShould {

    @Mock
    private lateinit var request: Request

    @Mock
    private lateinit var response: Response

    @Mock
    private lateinit var loginService: LoginService

    private val tom = aUser().build()
    private val userCredentials = UserCredentials(tom.username, tom.password)

    private lateinit var loginApi: LoginApi

    @BeforeEach
    fun setUp() {
        loginApi = LoginApi(loginService)
    }

    @Test
    fun perform_a_login() {
        given(request.body()).willReturn(jsonContaining(userCredentials))
        given(loginService.login(userCredentials)).willReturn(tom)

        loginApi.login(request, response)

        verify(loginService).login(userCredentials)
    }

    @Test
    fun return_json_containing_logged_in_user() {
        given(request.body()).willReturn(jsonContaining(userCredentials))
        given(loginService.login(userCredentials)).willReturn(tom)

        val result = loginApi.login(request, response)

        verify(response).status(200)
        verify(response).type("application/json")
        assertThat(result).isEqualTo(jsonContaining(tom))
    }

    @Test
    fun return_error_when_logging_in_with_invalid_credentials() {
        given(request.body()).willReturn(jsonContaining(userCredentials))
        given(loginService.login(userCredentials)).willThrow(InvalidUserCredentialsException::class.java)

        val result = loginApi.login(request, response)

        verify(response).status(400)
        assertThat(result).isEqualTo("Invalid credentials.")
    }

    private fun jsonContaining(user: User): String {
        return JsonObject()
            .add("id", user.id)
            .add("username", user.username)
            .add("about", user.about)
            .toString()
    }

    private fun jsonContaining(userCredentials: UserCredentials): String {
        return JsonObject()
            .add("username", userCredentials.username)
            .add("password", userCredentials.password)
            .toString()
    }
}