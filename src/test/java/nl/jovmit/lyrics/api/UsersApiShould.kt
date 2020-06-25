package nl.jovmit.lyrics.api

import com.eclipsesource.json.JsonObject
import nl.jovmit.lyrics.domain.users.User
import nl.jovmit.lyrics.domain.users.UserData
import nl.jovmit.lyrics.domain.users.UserService
import nl.jovmit.lyrics.domain.users.UsernameAlreadyInUseException
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
import java.util.*

@ExtendWith(MockitoExtension::class)
class UsersApiShould {

    @Mock
    private lateinit var response: Response

    @Mock
    private lateinit var request: Request

    @Mock
    private lateinit var userService: UserService

    private val userId = UUID.randomUUID().toString()
    private val username = "Tom"
    private val password = "alksjd12ealksjd"
    private val about = "About Tom"
    private val userData = UserData(username, password, about)
    private val user = User(userId, username, password, about)

    private lateinit var usersApi: UsersApi

    @BeforeEach
    fun setUp() {
        usersApi = UsersApi(userService)
    }

    @Test
    fun create_a_user() {
        given(userService.createUser(userData)).willReturn(user)
        given(request.body()).willReturn(jsonContaining(userData))

        usersApi.createUser(request, response)

        verify(userService).createUser(userData)
    }

    @Test
    fun return_json_containing_newly_created_user() {
        given(request.body()).willReturn(jsonContaining(userData))
        given(userService.createUser(userData)).willReturn(user)

        val result = usersApi.createUser(request, response)

        verify(response).status(201)
        verify(response).type("application/json")
        assertThat(jsonContaining(user)).isEqualTo(result)
    }

    @Test
    fun return_error_when_creating_a_duplicate_user() {
        given(request.body()).willReturn(jsonContaining(userData))
        given(userService.createUser(userData)).willThrow(UsernameAlreadyInUseException::class.java)

        val result = usersApi.createUser(request, response)

        verify(response).status(400)
        assertThat(result).isEqualTo("Username already in use.")
    }

    private fun jsonContaining(user: User): String {
        return JsonObject()
            .add("id", user.id)
            .add("username", user.username)
            .add("about", user.about)
            .toString()
    }

    private fun jsonContaining(userData: UserData): String {
        return JsonObject()
            .add("username", userData.username)
            .add("password", userData.password)
            .add("about", userData.about)
            .toString()
    }
}